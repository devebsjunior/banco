package br.com.arq.rules.core.catalog;

import java.math.BigDecimal;

import br.com.arq.exception.SaldoInsuficienteException;
import br.com.arq.model.Conta;
import br.com.arq.rules.core.Rule;
import br.com.arq.rules.core.RuleBuilder;

/**
 * Catálogo de regras de Conta.
 * Centraliza todas as validações de negócio relacionadas a contas.
 */
public final class ContaRules {

    public static final String VALOR = "VALOR";

    private ContaRules() {}

    /**
     * Regra que valida se o valor é válido.
     * Valor deve ser diferente de null e maior que zero.
     */
    public static Rule valorInvalido() {

        return RuleBuilder
                .when("Valor deve ser maior que zero",
                        facts -> {
                            BigDecimal valor = facts.get("VALOR");

                            return valor == null || valor.compareTo(BigDecimal.ZERO) <= 0;
                        }
                )
                .then(facts -> {
                    throw new IllegalArgumentException("Valor deve ser maior que zero");
                });
    }

    /**
     * Regra que valida saldo insuficiente.
     */
    public static Rule saldoInsuficiente() {

        return RuleBuilder
                .when("Saldo deve ser suficiente para operação",
                        facts -> {

                            Conta conta = facts.get(Conta.class);
                            BigDecimal valor = facts.get(BigDecimal.class);

                            // 🔒 Regra defensiva padronizada
                            if (conta == null) {
                                throw new IllegalStateException("Conta não informada nas Facts");
                            }

                            if (valor == null) {
                                throw new IllegalStateException("Valor não informado nas Facts");
                            }

                            return conta.getSaldo().compareTo(valor) < 0;
                        }
                )
                .then(facts -> {
                    throw new SaldoInsuficienteException();
                });
    }

    /**
     * Regra que valida saldo inicial na criação de conta.
     */
    public static Rule saldoInicialInvalido() {

        return RuleBuilder
                .when("Saldo inicial não pode ser negativo",
                        facts -> {
                            BigDecimal valor = facts.get("VALOR");
                            return valor == null || valor.compareTo(BigDecimal.ZERO) < 0;
                        }
                )
                .then(facts -> {
                    throw new IllegalArgumentException("Saldo inicial não pode ser negativo");
                });
    }


}