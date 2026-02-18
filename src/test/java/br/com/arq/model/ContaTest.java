package br.com.arq.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

class ContaTest {

    @Test
    @DisplayName("Deve aumentar o saldo ao creditar um valor positivo")
    void deveCreditarComSucesso() {
        Conta conta = new Conta();
        conta.setSaldo(new BigDecimal("100.00"));

        conta.creditar(new BigDecimal("50.00"));

        assertEquals(new BigDecimal("150.00"), conta.getSaldo());
    }

    @Test
    @DisplayName("Deve diminuir o saldo ao debitar um valor válido")
    void deveDebitarComSucesso() {
        Conta conta = new Conta();
        conta.setSaldo(new BigDecimal("100.00"));

        conta.debitar(new BigDecimal("30.00"));

        assertEquals(new BigDecimal("70.00"), conta.getSaldo());
    }

    @Test
    @DisplayName("Deve lançar exceção ao debitar valor maior que o saldo")
    void deveLancarExcecaoSaldoInsuficiente() {
        Conta conta = new Conta();
        conta.setSaldo(new BigDecimal("10.00"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            conta.debitar(new BigDecimal("20.00"));
        });

        assertEquals("Saldo insuficiente", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao creditar valor negativo ou zero")
    void deveLancarExcecaoCreditoInvalido() {
        Conta conta = new Conta();
        conta.setSaldo(BigDecimal.ZERO);

        assertThrows(RuntimeException.class, () -> conta.creditar(new BigDecimal("-5.00")));
        assertThrows(RuntimeException.class, () -> conta.creditar(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Deve testar o controle de versão (Optimistic Lock)")
    void deveTestarVersionamento() {
        Conta conta = new Conta();
        conta.setVersion(1L);
        assertEquals(1L, conta.getVersion());
    }
}