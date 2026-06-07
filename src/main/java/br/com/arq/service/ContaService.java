package br.com.arq.service;

import br.com.arq.dto.*;
import br.com.arq.enums.TipoTransacao;
import br.com.arq.exception.ContaNaoEncontradaException;
import br.com.arq.mapper.ContaMapper;
import br.com.arq.mapper.TransacaoMapper;
import br.com.arq.model.Cliente;
import br.com.arq.model.Conta;
import br.com.arq.model.Transacao;
import br.com.arq.repository.ClienteRepository;
import br.com.arq.repository.ContaRepository;
import br.com.arq.repository.TransacaoRepository;
import br.com.arq.rules.core.Facts;
import br.com.arq.rules.core.Rule;
import br.com.arq.rules.core.RuleEngine;
import br.com.arq.rules.core.RuleResult;
import br.com.arq.rules.core.catalog.ContaRules;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;
    private final ClienteRepository clienteRepository;
    private final AppLogService logService;
    private final AuditService auditService;


    @Transactional
    public Conta criarConta(ContaRequestDTO dto) {

        long inicio = System.currentTimeMillis();

        try {
            logService.info("Iniciando criação de conta", "ContaService");

            String senhaHash = BCrypt.hashpw(dto.senha(), BCrypt.gensalt());

             Cliente cliente = clienteRepository.findByCpf(dto.cpf())
                    .orElseGet(() -> {
                        Cliente novo = new Cliente();
                        novo.setNome(dto.nome());
                        novo.setCpf(dto.cpf());
                        novo.setEmail(dto.email());
                        return clienteRepository.save(novo);
                    });


            Facts facts = new Facts()
                    .add(BigDecimal.class, dto.saldo());

            RuleEngine.builder()
                    .facts(facts)
                    .rule(ContaRules.saldoInicialInvalido())
                    .logService(logService)
                    .auditService(auditService)
                    .build()
                    .run();

            Conta conta = new Conta();
            conta.setNumeroConta(dto.numeroConta());
            conta.setPerfil(dto.perfil());
            conta.setSaldo(dto.saldo());
            conta.setSenha(senhaHash);
            conta.setCliente(cliente);

            Conta salva = contaRepository.save(conta);

            logService.info("Conta criada com sucesso", "ContaService");
            auditService.registrar(
                    "user",
                    "USER",
                    "CRIAR_CONTA",
                    true,
                    "Conta criada",
                    null,
                    "ContaService",
                    tempo(inicio)
            );

            return salva;

        } catch (Exception ex) {
            logService.error("Erro ao criar conta", "ContaService", ex.getMessage());
            auditService.registrar(
                    "user",
                    "USER",
                    "CRIAR_CONTA",
                    false,
                    ex.getMessage(),
                    null,
                    "ContaService",
                    tempo(inicio)
            );
            throw ex;
        }
    }

    @Transactional
    public void depositar(OperacaoBancariaDTO dto) {

        long inicio = System.currentTimeMillis();

        try {
            logService.info("Iniciando depósito", "ContaService");

            Conta conta = contaRepository.findByNumeroContaWithLock(dto.numeroConta())
                    .orElseThrow(() -> new ContaNaoEncontradaException(dto.numeroConta()));


            if (!conta.getNomeBanco().equalsIgnoreCase(dto.banco())) {
                throw new RuntimeException("Banco inválido");
            }

            if (!conta.getAgencia().equals(dto.agencia())) {
                throw new RuntimeException("Agência inválida");
            }

            Facts facts = new Facts()
                    .add(BigDecimal.class, dto.valor());

            RuleEngine.builder()
                    .facts(facts)
                    .rule(ContaRules.valorInvalido())
                    .logService(logService)
                    .auditService(auditService)
                    .build()
                    .run();

            conta.creditar(dto.valor());
            contaRepository.save(conta);

            registrarTransacao(conta, TipoTransacao.DEPOSITO, dto.valor());

            logService.info("Depósito realizado com sucesso", "ContaService");

            auditService.registrar(
                    "user",
                    "USER",
                    "DEPOSITO",
                    true,
                    "Depósito realizado",
                    null,
                    "ContaService",
                    tempo(inicio)
            );

        } catch (Exception ex) {

            logService.error("Erro no depósito", "ContaService", ex.getMessage());

            auditService.registrar(
                    "user",
                    "USER",
                    "DEPOSITO",
                    false,
                    ex.getMessage(),
                    null,
                    "ContaService",
                    tempo(inicio)
            );

            throw ex;
        }
    }



    @Transactional
    public void transferir(TransferenciaDTO dto) {

        long inicio = System.currentTimeMillis();

        try {
            logService.info("Iniciando transferência", "ContaService");

            Conta origem = contaRepository.findByNumeroContaWithLock(dto.contaOrigem())
                    .orElseThrow(() -> new ContaNaoEncontradaException(dto.contaOrigem()));

            Conta destino = contaRepository.findByNumeroContaWithLock(dto.contaDestino())
                    .orElseThrow(() -> new ContaNaoEncontradaException(dto.contaDestino()));

            if (!origem.getNomeBanco().equalsIgnoreCase(dto.bancoOrigem())) {
                throw new RuntimeException("Banco origem inválido");
            }

            if (!origem.getAgencia().equals(dto.agenciaOrigem())) {
                throw new RuntimeException("Agência origem inválida");
            }


            if (!destino.getNomeBanco().equalsIgnoreCase(dto.bancoDestino())) {
                throw new RuntimeException("Banco destino inválido");
            }

            if (!destino.getAgencia().equals(dto.agenciaDestino())) {
                throw new RuntimeException("Agência destino inválida");
            }

            Facts facts = new Facts()
                    .add(Conta.class, origem)
                    .add(BigDecimal.class, dto.valor());

            RuleEngine.builder()
                    .facts(facts)
                    .rule(ContaRules.valorInvalido())
                    .rule(ContaRules.saldoInsuficiente())
                    .logService(logService)
                    .auditService(auditService)
                    .build()
                    .run();

            origem.debitar(dto.valor());
            destino.creditar(dto.valor());

            contaRepository.save(origem);
            contaRepository.save(destino);

            registrarTransacao(origem, TipoTransacao.TRANSFERENCIA_ENVIADA, dto.valor());
            registrarTransacao(destino, TipoTransacao.TRANSFERENCIA_RECEBIDA, dto.valor());

            logService.info("Transferência realizada com sucesso", "ContaService");

            auditService.registrar(
                    "user",
                    "USER",
                    "TRANSFERENCIA",
                    true,
                    "Transferência realizada",
                    null,
                    "ContaService",
                    tempo(inicio)
            );

        } catch (Exception ex) {

            logService.error("Erro na transferência", "ContaService", ex.getMessage());

            auditService.registrar(
                    "user",
                    "USER",
                    "TRANSFERENCIA",
                    false,
                    ex.getMessage(),
                    null,
                    "ContaService",
                    tempo(inicio)
            );

            throw ex;
        }
    }

    @Transactional
    public void sacar(OperacaoBancariaDTO dto) {

        long inicio = System.currentTimeMillis();

        try {
            logService.info("Iniciando saque", "ContaService");

            Conta conta = contaRepository.findByNumeroContaWithLock(dto.numeroConta())
                    .orElseThrow(() -> new ContaNaoEncontradaException(dto.numeroConta()));

            // ✅ valida banco
            if (!conta.getNomeBanco().equalsIgnoreCase(dto.banco())) {
                throw new RuntimeException("Banco inválido");
            }

            // ✅ valida agência
            if (!conta.getAgencia().equals(dto.agencia())) {
                throw new RuntimeException("Agência inválida");
            }

            Facts facts = new Facts()
                    .add(Conta.class, conta)
                    .add(BigDecimal.class, dto.valor());

            RuleEngine.builder()
                    .facts(facts)
                    .rule(ContaRules.valorInvalido())
                    .rule(ContaRules.saldoInsuficiente())
                    .logService(logService)
                    .auditService(auditService)
                    .build()
                    .run();

            conta.debitar(dto.valor());
            contaRepository.save(conta);

            registrarTransacao(conta, TipoTransacao.SAQUE, dto.valor());

            logService.info("Saque realizado com sucesso", "ContaService");

            auditService.registrar(
                    "user",
                    "USER",
                    "SAQUE",
                    true,
                    "Saque realizado",
                    null,
                    "ContaService",
                    tempo(inicio)
            );

        } catch (Exception ex) {

            logService.error("Erro no saque", "ContaService", ex.getMessage());

            auditService.registrar(
                    "user",
                    "USER",
                    "SAQUE",
                    false,
                    ex.getMessage(),
                    null,
                    "ContaService",
                    tempo(inicio)
            );

            throw ex;
        }
    }

    @Transactional
    public List<TransacaoDTO> buscarExtrato(String numeroConta) {

        Conta conta = contaRepository.findByNumeroConta(numeroConta)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroConta));

        return transacaoRepository.findByNumeroContaOrderByDataHoraDesc(conta.getNumeroConta())
                .stream()
                .map(TransacaoMapper.TO_DTO)
                .toList();
    }

    private TransacaoDTO registrarTransacao(
            Conta conta,
            TipoTransacao tipo,
            BigDecimal valor
    ) {
        Transacao t = new Transacao();

        t.setConta(conta);
        t.setTipo(tipo);
        t.setValor(valor);

        Transacao salva = transacaoRepository.save(t);

        return TransacaoMapper.TO_DTO.apply(salva);
    }

    public ContaDTO buscarPorNumero(String numero) {

        Conta conta = contaRepository.findByNumeroConta(numero)
                .orElseThrow(() -> new ContaNaoEncontradaException(numero));
        return ContaMapper.TO_DTO.apply(conta);
    }


    private void executarRegras(Facts facts, Rule... rules) {

        List<RuleResult> results = RuleEngine.builder()
                .facts(facts)
                .rules(List.of(rules))
                .build()
                .run();

        for (RuleResult result : results) {
            if (!result.success()) {
                logService.warn(
                        "Falha na regra: " + result.ruleName(),
                        "RuleEngine"
                );
                throw new IllegalArgumentException(result.message());
            }
        }
    }

    private long tempo(long inicio) {
        return System.currentTimeMillis() - inicio;
    }

}
