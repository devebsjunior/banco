package br.com.arq.rules.core;

import br.com.arq.service.AppLogService;
import br.com.arq.service.AuditService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

import java.util.List;

/**
 * Motor responsável por executar múltiplas regras.
 * Recebe uma lista de regras e um conjunto de fatos,
 * executando cada regra em sequência.
 */
@Builder
@RequiredArgsConstructor
public class RuleEngine {


        private final Facts facts;

        @Singular
        private final List<Rule> rules;

        private final AppLogService logService;
        private final AuditService auditService;

        public List<RuleResult> run() {

            List<RuleResult> results = new java.util.ArrayList<>();

            for (Rule rule : rules) {

                long inicio = System.currentTimeMillis();

                RuleResult result = rule.execute(facts);
                results.add(result);

                if (logService != null) {
                    if (result.success()) {
                        logService.info("Regra OK: " + rule.name(), "RuleEngine");
                    } else {
                        logService.warn("Regra falhou: " + rule.name(), "RuleEngine");
                    }
                }

                if (auditService != null) {
                    auditService.registrar(
                            "user",
                            "USER",
                            "RULE_EXECUTION",
                            result.success(),
                            result.message(),
                            rule.name(),
                            "RuleEngine",
                            System.currentTimeMillis() - inicio
                    );
                }

                if (!result.success()) {
                    throw new IllegalStateException(result.message());
                }
            }

            return results;
        }
 }



