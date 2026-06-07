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
import br.com.arq.rules.core.catalog.ContaRules;
import br.com.arq.utils.FactNames;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaService {

    private static final Logger logger = LoggerFactory.getLogger(ContaService.class);

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;
    private final ClienteRepository clienteRepository;
    private final AppLogService logService;
    private final AuditService auditService;


    @Transactional
    public Conta criarConta(ContaRequestDTO dto) {
        long inicio = System.currentTimeMillis();

        try {
            logger.info("Criando conta - CPF: {}, Numero: {}", dto.cpf(), dto.numeroConta());
            logService.info("Iniciando criação de conta", "ContaService");

            String senhaHash = BCrypt.hashpw(dto.senha(), BCrypt.gensalt());
            logger.debug("Senha criptografada com BCrypt");

            Cliente cliente = clienteRepository.findByCpf(dto.cpf())
                    .orElseGet(() -> {
                        logger.debug("Cliente nao encontrado, criando novo - CPF: {}", dto.cpf());
                        Cliente novo = new Cliente();
                        novo.setNome(dto.nome());
                        novo.setCpf(dto.cpf());
                        novo.setEmail(dto.email());
                        return clienteRepository.save(novo);
                    });

            logger.debug("Cliente associado - CPF: {}, Nome: {}", cliente.getCpf(), cliente.getNome());

            Facts facts = new Facts()
                    .add(FactNames.VALOR, dto.saldo());

            RuleEngine.builder()
                    .facts(facts)
                    .rule(ContaRules.saldoInicialInvalido())
                    .logService(logService)
                    .auditService(auditService)
                    .build()
                    .run();

            logger.debug("Regra de saldo inicial validada");

            Conta conta = new Conta();
            conta.setNumeroConta(dto.numeroConta());
            conta.setPerfil(dto.perfil());
            conta.setSaldo(dto.saldo());
            conta.setSenha(senhaHash);
            conta.setCliente(cliente);

            Conta salva = contaRepository.save(conta);
            logger.info("Conta criada com sucesso - Numero: {}, Saldo: {}, Duracao: {}ms",
                dto.numeroConta(), dto.saldo(), System.currentTimeMillis() - inicio);

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
            logger.error("Erro ao criar conta - CPF: {}, Numero: {}",
                dto.cpf(), dto.numeroConta(), ex);
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
            logger.info("Deposito iniciado - Conta: {}, Valor: R$ {}",
                dto.numeroConta(), dto.valor());
            logService.info("Iniciando depósito", "ContaService");

            Conta conta = contaRepository.findByNumeroContaWithLock(dto.numeroConta())
                    .orElseThrow(() -> new ContaNaoEncontradaException(dto.numeroConta()));

            logger.debug("Conta localizada: {}, Saldo Atual: R$ {}",
                dto.numeroConta(), conta.getSaldo());

            if (!conta.getNomeBanco().equalsIgnoreCase(dto.banco())) {
                logger.warn("Banco invalido - Esperado: {}, Recebido: {}",
                    conta.getNomeBanco(), dto.banco());
                throw new RuntimeException("Banco inválido");
            }

            if (!conta.getAgencia().equals(dto.agencia())) {
                logger.warn("Agencia invalida - Esperada: {}, Recebida: {}",
                    conta.getAgencia(), dto.agencia());
                throw new RuntimeException("Agência inválida");
            }

            Facts facts = new Facts()
                    .add(FactNames.VALOR, dto.valor());

            RuleEngine.builder()
                    .facts(facts)
                    .rule(ContaRules.valorInvalido())
                    .logService(logService)
                    .auditService(auditService)
                    .build()
                    .run();

            logger.debug("Regra de valor validada");

            conta.creditar(dto.valor());
            contaRepository.save(conta);

            registrarTransacao(conta, TipoTransacao.DEPOSITO, dto.valor());

            logger.info("Deposito concluido - Conta: {}, Valor: R$ {}, Novo Saldo: R$ {}, Duracao: {}ms",
                dto.numeroConta(), dto.valor(), conta.getSaldo(),
                System.currentTimeMillis() - inicio);

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
            logger.error("Erro ao depositar - Conta: {}, Valor: R$ {}, Duracao: {}ms",
                dto.numeroConta(), dto.valor(), System.currentTimeMillis() - inicio, ex);
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
            logger.info("Transferencia iniciada - De: {} Para: {}, Valor: R$ {}",
                dto.contaOrigem(), dto.contaDestino(), dto.valor());
            logService.info("Iniciando transferência", "ContaService");

            Conta origem = contaRepository.findByNumeroContaWithLock(dto.contaOrigem())
                    .orElseThrow(() -> new ContaNaoEncontradaException(dto.contaOrigem()));

            Conta destino = contaRepository.findByNumeroContaWithLock(dto.contaDestino())
                    .orElseThrow(() -> new ContaNaoEncontradaException(dto.contaDestino()));

            logger.debug("Contas localizadas - Origem: {}, Destino: {}, Saldo Origem: R$ {}",
                dto.contaOrigem(), dto.contaDestino(), origem.getSaldo());

            validarBancoAgencia(origem, dto.bancoOrigem(), dto.agenciaOrigem(), "origem");
            validarBancoAgencia(destino, dto.bancoDestino(), dto.agenciaDestino(), "destino");

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

            logger.debug("Regras de transferencia validadas");

            origem.debitar(dto.valor());
            destino.creditar(dto.valor());

            contaRepository.save(origem);
            contaRepository.save(destino);

            registrarTransacao(origem, TipoTransacao.TRANSFERENCIA_ENVIADA, dto.valor());
            registrarTransacao(destino, TipoTransacao.TRANSFERENCIA_RECEBIDA, dto.valor());

            logger.info("Transferencia concluida - De: {} Para: {}, Valor: R$ {}, Novo Saldo Origem: R$ {}, Duracao: {}ms",
                dto.contaOrigem(), dto.contaDestino(), dto.valor(), origem.getSaldo(),
                System.currentTimeMillis() - inicio);

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
            logger.error("Erro na transferencia - De: {} Para: {}, Valor: R$ {}, Duracao: {}ms",
                dto.contaOrigem(), dto.contaDestino(), dto.valor(),
                System.currentTimeMillis() - inicio, ex);

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
            logger.info("Saque iniciado - Conta: {}, Valor: R$ {}",
                dto.numeroConta(), dto.valor());
            logService.info("Iniciando saque", "ContaService");

            Conta conta = contaRepository.findByNumeroContaWithLock(dto.numeroConta())
                    .orElseThrow(() -> new ContaNaoEncontradaException(dto.numeroConta()));

            logger.debug("Conta localizada: {}, Saldo: R$ {}",
                dto.numeroConta(), conta.getSaldo());

            validarBancoAgencia(conta, dto.banco(), dto.agencia(), "saque");

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

            logger.debug("Regras de saque validadas");

            conta.debitar(dto.valor());
            contaRepository.save(conta);

            registrarTransacao(conta, TipoTransacao.SAQUE, dto.valor());

            logger.info("Saque concluido - Conta: {}, Valor: R$ {}, Novo Saldo: R$ {}, Duracao: {}ms",
                dto.numeroConta(), dto.valor(), conta.getSaldo(),
                System.currentTimeMillis() - inicio);

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
            logger.error("Erro no saque - Conta: {}, Valor: R$ {}, Duracao: {}ms",
                dto.numeroConta(), dto.valor(), System.currentTimeMillis() - inicio, ex);

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
        try {
            logger.debug("Buscando extrato - Conta: {}", numeroConta);

            Conta conta = contaRepository.findByNumeroConta(numeroConta)
                    .orElseThrow(() -> new ContaNaoEncontradaException(numeroConta));

            List<TransacaoDTO> extrato = transacaoRepository.findByNumeroContaOrderByDataHoraDesc(conta.getNumeroConta())
                    .stream()
                    .map(TransacaoMapper.TO_DTO)
                    .toList();

            logger.info("Extrato recuperado - Conta: {}, Total de transacoes: {}",
                numeroConta, extrato.size());

            return extrato;
        } catch (Exception ex) {
            logger.error("Erro ao buscar extrato - Conta: {}", numeroConta, ex);
            throw ex;
        }
    }

    public ContaDTO buscarPorNumero(String numero) {
        try {
            logger.debug("Consultando conta - Numero: {}", numero);

            Conta conta = contaRepository.findByNumeroConta(numero)
                    .orElseThrow(() -> new ContaNaoEncontradaException(numero));

            ContaDTO contaDTO = ContaMapper.TO_DTO.apply(conta);

            logger.debug("Conta localizada - Numero: {}, Saldo: R$ {}",
                numero, conta.getSaldo());

            return contaDTO;
        } catch (Exception ex) {
            logger.error("Erro ao consultar conta - Numero: {}", numero, ex);
            throw ex;
        }
    }

    private void validarBancoAgencia(Conta conta, String banco, String agencia, String tipo) {
        if (!conta.getNomeBanco().equalsIgnoreCase(banco)) {
            logger.warn("Banco invalido em {} - Esperado: {}, Recebido: {}",
                tipo, conta.getNomeBanco(), banco);
            throw new RuntimeException("Banco inválido");
        }

        if (!conta.getAgencia().equals(agencia)) {
            logger.warn("Agencia invalida em {} - Esperada: {}, Recebida: {}",
                tipo, conta.getAgencia(), agencia);
            throw new RuntimeException("Agência inválida");
        }
    }

    private void registrarTransacao(Conta conta, TipoTransacao tipo, BigDecimal valor) {
        try {
            Transacao t = new Transacao();
            t.setConta(conta);
            t.setTipo(tipo);
            t.setValor(valor);

            transacaoRepository.save(t);

            logger.debug("Transacao registrada - Tipo: {}, Valor: R$ {}, Conta: {}",
                tipo, valor, conta.getNumeroConta());

        } catch (Exception ex) {
            logger.error("Erro ao registrar transacao - Tipo: {}, Valor: R$ {}",
                tipo, valor, ex);
            throw ex;
        }
    }



    private void executarRegras(
            Facts facts,
            Rule... rules
    ) {

        RuleEngine.RuleEngineBuilder builder =
                RuleEngine.builder()
                        .facts(facts)
                        .logService(logService)
                        .auditService(auditService);

        for (Rule rule : rules) {
            builder.rule(rule);
        }

        builder.build().run();
    }

    private void auditoriaSucesso(
            String operacao,
            String mensagem,
            long inicio
    ) {

        auditService.registrar(
                "user",
                "USER",
                operacao,
                true,
                mensagem,
                null,
                "ContaService",
                tempo(inicio)
        );
    }

    private void auditoriaErro(
            String operacao,
            Exception ex,
            long inicio
    ) {

        auditService.registrar(
                "user",
                "USER",
                operacao,
                false,
                ex.getMessage(),
                null,
                "ContaService",
                tempo(inicio)
        );
    }

    private long tempo(long inicio) {
        return System.currentTimeMillis()
                - inicio;
    }

        private Cliente obterOuCriarCliente(
                ContaRequestDTO dto
        ) {

            return clienteRepository
                    .findByCpf(dto.cpf())
                    .orElseGet(() -> {

                        logger.info(
                                "Cliente não encontrado. Criando novo CPF={}",
                                dto.cpf()
                        );

                        Cliente cliente =
                                new Cliente();

                        cliente.setNome(dto.nome());
                        cliente.setCpf(dto.cpf());
                        cliente.setEmail(dto.email());

                        return clienteRepository.save(cliente);
                    });
        }


        private Conta construirConta(
                ContaRequestDTO dto,
                Cliente cliente
        ) {

            Conta conta =
                    new Conta();

            conta.setNumeroConta(
                    dto.numeroConta()
            );

            conta.setPerfil(
                    dto.perfil()
            );

            conta.setSaldo(
                    dto.saldo()
            );

            conta.setSenha(
                    BCrypt.hashpw(
                            dto.senha(),
                            BCrypt.gensalt()
                    )
            );

            conta.setCliente(
                    cliente
            );

            return conta;
        }



}
