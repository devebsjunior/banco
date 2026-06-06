package br.com.arq.rules.core;

import br.com.arq.rules.core.interfaces.Then;
import br.com.arq.rules.core.interfaces.When;

/**
 * Representa uma regra de negócio composta por:
 * - nome da regra
 * - condição (When)
 * - ação (Then)
 * A regra é executada avaliando a condição.
 * Se a condição for verdadeira, a ação é executada.
 */
public record Rule(
        String name,
        When when,
        Then then
) {

    public RuleResult execute(Facts facts) {
        try {
            if (when.test(facts)) {
                then.apply(facts);
            }
            return new RuleResult(name, true, "OK");
        } catch (Exception e) {
            return new RuleResult(name, false, e.getMessage());
        }
    }

}