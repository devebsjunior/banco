package br.com.arq.rules.core;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleEngineTest {

    @Test
    void deveExecutarTodasAsRegras() {

        Facts facts =
                new Facts();

        Rule r1 =
                RuleBuilder
                        .when(
                                "r1",
                                f -> true
                        )
                        .then(
                                f -> {}
                        );

        Rule r2 =
                RuleBuilder
                        .when(
                                "r2",
                                f -> true
                        )
                        .then(
                                f -> {}
                        );

        List<RuleResult> results =
                RuleEngine.builder()
                        .facts(facts)
                        .rule(r1)
                        .rule(r2)
                        .build()
                        .run();

        assertEquals(
                2,
                results.size()
        );
    }

    @Test
    void deveFalharQuandoRegraFalhar() {

        Facts facts =
                new Facts();

        Rule rule =
                RuleBuilder
                        .when(
                                "falha",
                                f -> true
                        )
                        .then(
                                f -> {
                                    throw new RuntimeException(
                                            "erro"
                                    );
                                }
                        );

        assertThrows(
                IllegalStateException.class,
                () -> RuleEngine.builder()
                        .facts(facts)
                        .rule(rule)
                        .build()
                        .run()
        );
    }
}
