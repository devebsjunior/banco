package br.com.arq.rules.core;


/**
 * Representa o resultado da execução de uma regra.
 * Armazena o nome da regra, se foi executada com sucesso
 * e uma mensagem descritiva.
 */
public record RuleResult(
        String ruleName,
        boolean success,
        String message
) {

}
