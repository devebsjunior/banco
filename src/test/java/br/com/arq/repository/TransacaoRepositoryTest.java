package br.com.arq.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import br.com.arq.model.Transacao;

@DataJpaTest
@ActiveProfiles("test") 
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransacaoRepositoryTest {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve buscar transações de uma conta ordenadas pela data mais recente")
    void deveBuscarTransacoesOrdenadas() throws InterruptedException {
        String numeroConta = "123789";

        Transacao t1 = Transacao.builder()
                .numeroConta(numeroConta)
                .tipo("DEPOSITO")
                .valor(new BigDecimal("100.00"))
                .build();

        Transacao t2 = Transacao.builder()
                .numeroConta(numeroConta)
                .tipo("SAQUE")
                .valor(new BigDecimal("50.00"))
                .build();

        entityManager.persist(t1);
        Thread.sleep(10); 
        entityManager.persist(t2);
        entityManager.flush();

        List<Transacao> resultado = transacaoRepository.findByNumeroContaOrderByDataHoraDesc(numeroConta);
        assertEquals(2, resultado.size());
        assertEquals("SAQUE", resultado.get(0).getTipo(), "A transação mais recente (t2) deve vir primeiro");
        assertEquals("DEPOSITO", resultado.get(1).getTipo(), "A transação mais antiga (t1) deve vir por último");
    }

    @Test
    @DisplayName("Deve retornar lista vazia para conta sem movimentações")
    void deveRetornarVazioParaContaSemTransacao() {
        List<Transacao> resultado = transacaoRepository.findByNumeroContaOrderByDataHoraDesc("999999");
        assertTrue(resultado.isEmpty());
    }
}