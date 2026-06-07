package br.com.arq.rules.core;


/**
 * Representa o resultado da execução de uma regra.
 * Armazena o nome da regra, se foi executada com sucesso
 * e uma mensagem descritiva.
 */
public record RuleResult(
      String ruleName,
      boolean success,
      boolean activated,
      String message,
      String exceptionType,
     long executionTimeMs
) {

    public static RuleResult success(
            String ruleName,
            boolean activated,
            long executionTimeMs
    ) {
        return new RuleResult(
                ruleName,
                true,
                activated,
                "OK",
                null,
                executionTimeMs
        );
    }

    public static RuleResult failure(
            String ruleName,
            Exception ex,
            long executionTimeMs
    ) {
        return new RuleResult(
                ruleName,
                false,
                true,
                ex.getMessage(),
                ex.getClass().getSimpleName(),
                executionTimeMs
        );
    }
}
