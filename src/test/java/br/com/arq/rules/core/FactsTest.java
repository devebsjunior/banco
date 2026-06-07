package br.com.arq.rules.core;


import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class FactsTest {

    @Test
    void deveAdicionarERecuperarPorClasse() {

        Facts facts = new Facts();

        facts.add(BigDecimal.class, BigDecimal.TEN);

        BigDecimal valor =
                facts.get(BigDecimal.class);

        assertEquals(
                BigDecimal.TEN,
                valor
        );
    }

    @Test
    void deveAdicionarERecuperarPorString() {

        Facts facts = new Facts();

        facts.add(
                "VALOR",
                BigDecimal.ONE
        );

        BigDecimal valor =
                facts.get("VALOR");

        assertEquals(
                BigDecimal.ONE,
                valor
        );
    }

    @Test
    void deveConterValor() {

        Facts facts = new Facts();

        facts.add(
                "CONTA",
                "123"
        );

        assertTrue(
                facts.contains("CONTA")
        );
    }

    @Test
    void deveRemoverValor() {

        Facts facts = new Facts();

        facts.add(
                "CONTA",
                "123"
        );

        facts.remove("CONTA");

        assertFalse(
                facts.contains("CONTA")
        );
    }
}