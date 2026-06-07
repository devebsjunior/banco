package br.com.arq.service;


import br.com.arq.dto.request.ContaRequestDTO;
import br.com.arq.dto.request.OperacaoBancariaDTO;
import br.com.arq.dto.TransferenciaDTO;
import br.com.arq.model.Conta;
import br.com.arq.repository.AppLogRepository;
import br.com.arq.repository.AuditRepository;
import br.com.arq.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ContaServiceIT {

    private static final Logger log =
            LoggerFactory.getLogger(ContaServiceIT.class);

    @Autowired
    private ContaService contaService;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private AppLogRepository appLogRepository;

    @BeforeEach
    void limparBanco() {

        contaRepository.deleteAll();
    }

    @Test
    void deveCriarConta() {

        ContaRequestDTO dto =
                new ContaRequestDTO(
                        "Edson",
                        "12345678901",
                        "edson@email.com",
                        "0001",
                        BigDecimal.valueOf(2000),
                         "USER",
                        "123456"
                );

        Conta conta =
                contaService.criarConta(dto);

        assertNotNull(conta);

        assertNotNull(conta.getId());

        assertEquals(
                BigDecimal.valueOf(1000),
                conta.getSaldo()
        );

        assertTrue(
                contaRepository
                        .findByNumeroConta("0001")
                        .isPresent()
        );
    }

    @Test
    void deveDepositar() {

        criarContaBase();

        OperacaoBancariaDTO dto =
                new OperacaoBancariaDTO(
                        "0001",
                        "Banco Mundial",
                        "0001",
                        BigDecimal.valueOf(500)
                );

        contaService.depositar(dto);

        Conta conta =
                contaRepository
                        .findByNumeroConta("0001")
                        .orElseThrow();

        assertEquals(
                BigDecimal.valueOf(1500),
                conta.getSaldo()
        );
    }

    @Test
    void deveSacar() {

        criarContaBase();

        OperacaoBancariaDTO dto =
                new OperacaoBancariaDTO(
                        "0001",
                        "Banco Mundial",
                        "0001",
                        BigDecimal.valueOf(200)
                );

        contaService.sacar(dto);

        Conta conta =
                contaRepository
                        .findByNumeroConta("0001")
                        .orElseThrow();

        assertEquals(
                BigDecimal.valueOf(800),
                conta.getSaldo()
        );
    }

    @Test
    void deveTransferir() {

        criarContaBase();

        criarContaDestino();

        contaService.transferir(
                new TransferenciaDTO(
                        "0001",
                        "0002",
                        "Banco Mundial",
                        "0001",
                        "Banco Mundial",
                        "0001",
                        BigDecimal.valueOf(100)
                )
        );

        Conta origem =
                contaRepository
                        .findByNumeroConta("0001")
                        .orElseThrow();

        Conta destino =
                contaRepository
                        .findByNumeroConta("0002")
                        .orElseThrow();

        System.out.println(
                "Saldo origem: " +
                        origem.getSaldo()
        );

        System.out.println(
                "Saldo destino: " +
                        destino.getSaldo()
        );

        assertEquals(
                BigDecimal.valueOf(900),
                origem.getSaldo()
        );

        assertEquals(
                BigDecimal.valueOf(1100),
                destino.getSaldo()
        );
    }
    @Test
    void deveGerarAuditoria() {

        long antes =
                auditRepository.count();

        criarContaBase();

        long depois =
                auditRepository.count();

        assertTrue(
                depois > antes
        );
    }


    @Test
    void deveLancarExcecaoQuandoSaldoInsuficiente() {

        criarContaBase();

        OperacaoBancariaDTO dto =
                new OperacaoBancariaDTO(
                        "0001",
                        "Banco Mundial",
                        "0001",
                        BigDecimal.valueOf(999999)
                );

        assertThrows(
                IllegalStateException.class,
                () -> contaService.sacar(dto)
        );
    }

    @Test
    void deveLancarExcecaoQuandoBancoInvalido() {

        criarContaBase();

        OperacaoBancariaDTO dto =
                new OperacaoBancariaDTO(
                        "0001",
                        "Banco Fake",
                        "0001",
                        BigDecimal.valueOf(100)
                );

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> contaService.depositar(dto)
                );

        assertEquals(
                "Banco inválido",
                ex.getMessage()
        );
    }

    @Test
    void deveGerarLog() {

        long antes =
                appLogRepository.count();

        criarContaBase();

        long depois =
                appLogRepository.count();

        assertTrue(
                depois > antes
        );
    }

    private void criarContaBase() {

        Conta conta =
                new Conta();

        conta.setNumeroConta("0001");
        conta.setNomeBanco("Banco Mundial");
        conta.setAgencia("0001");
        conta.setPerfil("CLIENTE");
        conta.setSaldo(BigDecimal.valueOf(1000));
        conta.setSenha("123");
        contaRepository.save(conta);
    }

    private void criarContaDestino() {

        Conta conta =
                new Conta();

        conta.setNumeroConta("0002");
        conta.setNomeBanco("Banco Mundial");
        conta.setAgencia("0001");
        conta.setPerfil("CLIENTE");
        conta.setSaldo(BigDecimal.valueOf(1000));
        conta.setSenha("123");
        contaRepository.save(conta);
    }

    @Test
    void deveLancarExcecaoQuandoAgenciaInvalida() {
        criarContaBase();
        OperacaoBancariaDTO dto =
                new OperacaoBancariaDTO(
                        "0001",
                        "Banco Mundial",
                        "9999",
                        BigDecimal.valueOf(100)
                );

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> contaService.depositar(dto)
                );
        assertEquals(
                "Agência inválida",
                ex.getMessage()
        );
    }

}