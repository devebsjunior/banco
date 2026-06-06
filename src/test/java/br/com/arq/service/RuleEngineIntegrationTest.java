package br.com.arq.service;

package br.com.arq.rules;

import br.com.arq.model.Conta;
import br.com.arq.rules.core.Facts;
import br.com.arq.rules.core.Rule;
import br.com.arq.rules.core.RuleEngine;
import br.com.arq.rules.catalog.ContaRules;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleEngineIntegrationTest {

    // =========================
    // ✅ TESTE VALOR INVALIDO
    // =========================
    @Test
    void deveFalharQuandoValorInvalido() {

        Facts facts = new Facts()
                .add(BigDecimal.class, BigDecimal.ZERO);

        RuleEngine engine = RuleEngine.builder()
                .facts(facts)
                .rules(List.of(
                        ContaRules.valorInvalido() // ✅ SUA RULE
                ))
                .build();

        assertThrows(IllegalArgumentException.class, engine::run);
    }

    // =========================
    // ✅ TESTE VALOR VALIDO
    // =========================
    @Test
    void devePassarQuandoValorValido() {

        Facts facts = new Facts()
                .add(BigDecimal.class, BigDecimal.TEN);

        RuleEngine engine = RuleEngine.builder()
                .facts(facts)
                .rules(List.of(
                        ContaRules.valorInvalido() // ✅ SUA RULE
                ))
                .build();

        assertDoesNotThrow(engine::run);
    }

    // =========================
    // ✅ TESTE SALDO INSUFICIENTE
    // =========================
    @Test
    void deveFalharQuandoSaldoInsuficiente() {

        Conta conta = new Conta();
        conta.setSaldo(BigDecimal.valueOf(100));

        Facts facts = new Facts()
                .add(Conta.class, conta)
                .add(BigDecimal.class, BigDecimal.valueOf(200));

        RuleEngine engine = RuleEngine.builder()
                .facts(facts)
                .rules(List.of(
                        ContaRules.saldoInsuficiente() // ✅ SUA RULE
                ))
                .build();

        assertThrows(Exception.class, engine::run);
    }

    // =========================
    // ✅ TESTE MULTIPLAS REGRAS
    // =========================
    @Test
    void deveExecutarMultiplasRegras() {

        Conta conta = new Conta();
        conta.setSaldo(BigDecimal.valueOf(1000));

        Facts facts = new Facts()
                .add(Conta.class, conta)
                .add(BigDecimal.class, BigDecimal.valueOf(100));

        RuleEngine engine = RuleEngine.builder()
                .facts(facts)
                .rules(List.of(
                        ContaRules.valorInvalido(),
                        ContaRules.saldoInsuficiente()
                ))
                .build();

        assertDoesNotThrow(engine::run);
    }
}