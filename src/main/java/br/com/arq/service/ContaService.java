package br.com.arq.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.arq.dto.ContaRequestDTO;
import br.com.arq.dto.TransferenciaDTO;
import br.com.arq.model.Cliente;
import br.com.arq.model.Conta;
import br.com.arq.model.Transacao;
import br.com.arq.repository.ClienteRepository;
import br.com.arq.repository.ContaRepository;
import br.com.arq.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContaService {

	private final ContaRepository contaRepository;
	private final TransacaoRepository transacaoRepository;
	private final ClienteRepository clienteRepository;

	@Transactional
	public Conta criarConta(ContaRequestDTO dto) {
	    String senhaHash = BCrypt.hashpw(dto.senha(), BCrypt.gensalt());
	    
	    System.out.println("DEBUG - Senha Pura: " + dto.senha());
	    System.out.println("DEBUG - Senha Hash: " + senhaHash);

	    Cliente cliente = clienteRepository.findByCpf(dto.cpf()).orElseGet(() -> {
	        Cliente novoCliente = new Cliente();
	        novoCliente.setNome(dto.nome());
	        novoCliente.setCpf(dto.cpf());
	        novoCliente.setEmail(dto.email());
	        return clienteRepository.save(novoCliente);
	    });

	    Conta novaConta = new Conta();
	    novaConta.setNumeroConta(dto.numeroConta());
	    novaConta.setPerfil(dto.perfil());
	    novaConta.setSaldo(dto.saldo());
	    novaConta.setSenha(senhaHash); 
	    novaConta.setCliente(cliente);

	    contaRepository.save(novaConta);
	    System.out.println(">>> SALVO NO BANCO COM SUCESSO!");
	    return contaRepository.save(novaConta);
	}

	@Transactional
	public void transferir(TransferenciaDTO dto) {
		this.executarTransferencia(dto.origem(), dto.destino(), dto.valor());
	}

	@Transactional
	private void executarTransferencia(String origem, String destino, BigDecimal valor) {
		Conta cOrigem = contaRepository.findByNumeroContaWithLock(origem)
				.orElseThrow(() -> new RuntimeException("Conta de origem não encontrada: " + origem));
		Conta cDestino = contaRepository.findByNumeroContaWithLock(destino)
				.orElseThrow(() -> new RuntimeException("Conta de destino não encontrada: " + destino));

		if (cOrigem.getSaldo().compareTo(valor) < 0) {
			throw new RuntimeException("Saldo insuficiente para transferência.");
		}

		cOrigem.debitar(valor);
		cDestino.creditar(valor);

		contaRepository.save(cOrigem);
		contaRepository.save(cDestino);
		registrarTransacao(origem, "TRANSFERENCIA ENVIADA PARA: " + destino, valor);
		registrarTransacao(destino, "TRANSFERENCIA RECEBIDA DE: " + origem, valor);
	}

	@Transactional
	public void depositar(String numero, BigDecimal valor) {
		Conta conta = contaRepository.findByNumeroContaWithLock(numero)
				.orElseThrow(() -> new RuntimeException("Conta não encontrada"));
		conta.creditar(valor);
		contaRepository.save(conta);
		registrarTransacao(numero, "DEPOSITO", valor);
	}

	@Transactional
	public void sacar(String numero, BigDecimal valor) {
		Conta conta = contaRepository.findByNumeroContaWithLock(numero)
				.orElseThrow(() -> new RuntimeException("Conta não encontrada: " + numero));

		if (conta.getSaldo().compareTo(valor) < 0) {
			throw new RuntimeException("Saldo insuficiente para realizar o saque.");
		}
		BigDecimal novoSaldo = conta.getSaldo().subtract(valor);
		conta.setSaldo(novoSaldo);
		contaRepository.save(conta);
		registrarTransacao(numero, "SAQUE", valor);
	}

	private void registrarTransacao(String numero, String tipo, BigDecimal valor) {
		Transacao t = new Transacao();
		t.setNumeroConta(numero);
		t.setTipo(tipo);
		t.setValor(valor);
		t.setDataHora(LocalDateTime.now());
		transacaoRepository.save(t);
	}

	public List<Transacao> buscarExtrato(String numero) {
		return transacaoRepository.findByNumeroContaOrderByDataHoraDesc(numero);
	}

	public List<Conta> listarTodas() {
		return contaRepository.findAll();
	}

	public List<Conta> listarApenasUsuarios() {
		return contaRepository.findByPerfil("usuario");
	}

}