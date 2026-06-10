package br.com.arq.service;

import br.com.arq.dto.request.ClienteRequestDTO;
import br.com.arq.model.Agencia;
import br.com.arq.model.Cliente;
import br.com.arq.model.Conta;
import br.com.arq.repository.AgenciaRepository;
import br.com.arq.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ClienteService {

  private final ClienteRepository repository;
  private final AppLogService logService;
  private final AuditService auditService;
  private final AgenciaRepository agenciaRepository;
  private final EmailService emailService;

  @Transactional
  public Cliente criar(ClienteRequestDTO dto) {

    long inicio = System.currentTimeMillis();

    try {
      repository.findByCpf(dto.cpf()).ifPresent(cliente -> {
        throw new RuntimeException("CPF já cadastrado");
      });

      Cliente cliente = Cliente.builder()
              .nome(dto.nome())
              .cpf(dto.cpf())
              .email(dto.email())
              .build();

      if (dto.enderecoCliente() != null) {
        br.com.arq.model.Endereco endereco = br.com.arq.model.Endereco.builder()
                .logradouro(dto.enderecoCliente().logradouro())
                .numero(dto.enderecoCliente().numero())
                .complemento(dto.enderecoCliente().complemento())
                .bairro(dto.enderecoCliente().bairro())
                .cidade(dto.enderecoCliente().cidade())
                .estado(dto.enderecoCliente().estado())
                .cep(dto.enderecoCliente().cep())
                .cliente(cliente)
                .build();

        cliente.setEndereco(endereco);
      }

      if (dto.conta() != null) {
        Agencia agencia = agenciaRepository.findById(dto.conta().agencia().id())
                .orElseThrow(() -> new RuntimeException("Agência associada não encontrada"));

        Conta conta = Conta.builder()
                .numeroConta(dto.conta().numeroConta())
                .saldo(new BigDecimal("0.00"))
                .agencia(agencia)
                .cliente(cliente)
                .perfil("CLIENTE")
                .senha(dto.senha())
                .build();

        cliente.setContas(List.of(conta));
      }

      Cliente salvo = repository.save(cliente);

      logService.info("Cliente criado CPF=" + dto.cpf(), "ClienteService");
      auditService.registrar("system", "CLIENTE",
              "CRIAR_CLIENTE", true,
              "Cliente criado com endereço e conta bancária", null,
              "ClienteService", tempo(inicio));
      emailService.enviarGmail(salvo.getEmail());
      return salvo;
    }
    catch (Exception ex) {
      logService.error("Erro ao criar cliente", "ClienteService", ex.getMessage());
      auditService.registrar("system", "CLIENTE", "CRIAR_CLIENTE", false, ex.getMessage(), null, "ClienteService", tempo(inicio));
      throw ex;
    }
  }

  public Cliente buscarPorId(Long id) {
    return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
  }

  public Cliente buscarPorCpf(String cpf) {
    return repository.findByCpf(cpf).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
  }

  @Transactional(readOnly = true)
  public List<Cliente> buscarTodos() {
    List<Cliente> clientes = repository.findAllComContas();
    clientes.forEach(c -> {
      if (c.getContas() != null) c.getContas().size();
      if (c.getEndereco() != null) c.getEndereco().getId();
    });
    return clientes;
  }

  @Transactional
  public void excluir(Long id) {
    Cliente cliente = buscarPorId(id);
    repository.delete(cliente);
    logService.info("Cliente removido CPF=" + cliente.getCpf(), "ClienteService");
  }

  private long tempo(long inicio) {
    return System.currentTimeMillis() - inicio;
  }


}