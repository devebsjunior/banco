package br.com.arq.service;

import br.com.arq.dto.request.ClienteRequestDTO;
import br.com.arq.model.Cliente;
import br.com.arq.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

  private final ClienteRepository repository;
  private final AppLogService logService;
  private final AuditService auditService;

  @Transactional
  public Cliente criar(ClienteRequestDTO dto) {

    long inicio = System.currentTimeMillis();

    try {
      repository.findByCpf(dto.cpf()).ifPresent(cliente -> {
        throw new RuntimeException("CPF já cadastrado");
      });

      Cliente cliente = Cliente.builder().nome(dto.nome()).cpf(dto.cpf()).email(dto.email()).build();
      Cliente salvo = repository.save(cliente);
      logService.info("Cliente criado CPF=" + dto.cpf(),
                      "ClienteService");
      auditService.registrar("system", "CLIENTE",
                             "CRIAR_CLIENTE", true,
                             "Cliente criado", null,
                             "ClienteService", tempo(inicio));
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

  public List<Cliente> buscarTodos() {
    return repository.findAll();
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