package br.com.arq.service;


import br.com.arq.model.Agencia;
import br.com.arq.model.Conta;
import br.com.arq.repository.ClienteRepository;
import br.com.arq.repository.ContaRepository;
import br.com.arq.repository.TransacaoRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private AppLogService logService;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private ContaService contaService;

    @Test
    @DisplayName("Não deve permitir saque se o saldo for insuficiente")
    void testeSaqueSaldoInsuficiente() {

        Agencia agencia = new Agencia();
        agencia.setNomeAgencia("Banco Teste");
        agencia.setNumeroAgencia("0001");
        agencia.setCodigo("001");

        entityManager.persist(agencia);


        Conta conta = new Conta();
        conta.setNumeroConta("123789");
        conta.setSaldo(new BigDecimal("100.00"));
        conta.setAgencia(agencia);

        when(contaRepository.findByNumeroContaWithLock("123789"))
                .thenReturn(Optional.of(conta));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                contaService.sacarSimples("123789", new BigDecimal("200.00"))
        );

        assertEquals("Saldo insuficiente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve realizar depósito com sucesso")
    void testeDepositoSucesso() {

        Conta conta = new Conta();
        conta.setNumeroConta("123789");
        conta.setSaldo(new BigDecimal("500.00"));

        Agencia agencia = new Agencia();
        agencia.setNomeAgencia("Banco Teste");
        agencia.setNumeroAgencia("0001");

        conta.setAgencia(agencia);

    }

}
