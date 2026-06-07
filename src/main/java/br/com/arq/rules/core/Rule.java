package br.com.arq.rules.core;

import br.com.arq.rules.core.interfaces.Then;
import br.com.arq.rules.core.interfaces.When;

/**
 * Representa uma regra de negócio.
 * Uma regra possui:
 * - Nome
 * - Condição (When)
 * - Ação (Then)
 * Durante a execução:
 * - Avalia a condição
 * - Executa a ação quando a condição for satisfeita
 * - Mede tempo de execução
 * - Retorna um RuleResult
 * - Captura falhas de forma controlada
 */
public  record Rule(
        String name,
        When when,
        Then then
) {
    public RuleResult execute(Facts facts) {

        long inicio = System.currentTimeMillis();

        try {

            boolean activated = when.test(facts);

            if (activated) {
                then.apply(facts);
            }

            return RuleResult.success(
                    name,
                    activated,
                    System.currentTimeMillis() - inicio
            );

        } catch (Exception ex) {

            return RuleResult.failure(
                    name,
                    ex,
                    System.currentTimeMillis() - inicio
            );
        }
    }
}