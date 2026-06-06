package br.com.arq.rules.core;

import br.com.arq.rules.core.interfaces.Then;
import br.com.arq.rules.core.interfaces.When;

/**
 * Builder responsável por criar uma regra.
 * Permite montar uma regra de forma fluente:
 * RuleBuilder.when(...).then(...)
 */
public class RuleBuilder {

    private final String name;
    private final When when;

    private RuleBuilder(String name, When when) {
        this.name = name;
        this.when = when;
    }

    /**
     * Define a condição da regra.
     *
     * @param name nome da regra
     * @param when condição que será avaliada
     * @return instancia do builder para continuar a criação
     */
    public static RuleBuilder when(String name, When when) {
        return new RuleBuilder(name, when);
    }

    /**
     * Define a ação da regra.
     *
     * @param then ação executada quando a condição for verdadeira
     * @return regra final criada
     */
    public Rule then(Then then) {
        return new Rule(name, when, then);
    }
}