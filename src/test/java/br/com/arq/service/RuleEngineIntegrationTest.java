package br.com.arq.service;


import br.com.arq.rules.core.Facts;
import br.com.arq.rules.core.RuleEngine;
import br.com.arq.rules.core.catalog.ContaRules;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class RuleEngineIntegrationTest {

    @Test
    void deveFalharQuandoValorInvalido() {

        Facts facts = new Facts()
                .add(BigDecimal.class, BigDecimal.ZERO);

        RuleEngine engine = RuleEngine.builder()
                .facts(facts)
                .rule(ContaRules.valorInvalido())
                .build();

        IllegalStateException ex =
                assertThrows(IllegalStateException.class, engine::run);

        assertTrue(ex.getMessage().contains("Valor deve ser maior que zero"));
    }
}