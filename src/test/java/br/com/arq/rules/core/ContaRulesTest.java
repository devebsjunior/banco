package br.com.arq.rules.core;

import br.com.arq.rules.core.catalog.ContaRules;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ContaRulesTest {

    @Test
    void deveValidarValorInvalido() {

        Facts facts =
                new Facts()
                        .add(
                                "VALOR",
                                BigDecimal.ZERO
                        );

        RuleResult result =
                ContaRules
                        .valorInvalido()
                        .execute(facts);

        assertFalse(
                result.success()
        );
    }

    @Test
    void deveAceitarValorValido() {

        Facts facts =
                new Facts()
                        .add(
                                "VALOR",
                                BigDecimal.TEN
                        );

        RuleResult result =
                ContaRules
                        .valorInvalido()
                        .execute(facts);

        assertTrue(
                result.success()
        );
    }
}