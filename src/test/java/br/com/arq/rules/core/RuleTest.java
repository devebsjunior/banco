package br.com.arq.rules.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RuleTest {


        @Test
        void deveExecutarRegraComSucesso() {

            Rule rule = RuleBuilder
                    .when(
                            "teste",
                            facts -> true
                    )
                    .then(
                            facts -> {}
                    );

            RuleResult result =
                    rule.execute(
                            new Facts()
                    );

            assertTrue(
                    result.success()
            );

            assertTrue(
                    result.activated()
            );
        }

        @Test
        void deveRetornarFalha() {

            Rule rule = RuleBuilder
                    .when(
                            "teste",
                            facts -> true
                    )
                    .then(
                            facts -> {
                                throw new RuntimeException(
                                        "erro"
                                );
                            }
                    );

            RuleResult result =
                    rule.execute(
                            new Facts()
                    );

            assertFalse(
                    result.success()
            );

            assertEquals(
                    "erro",
                    result.message()
            );
        }
    }