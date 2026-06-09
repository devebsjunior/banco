package br.com.arq.rules.core.catalog;

import br.com.arq.repository.AgenciaRepository;
import br.com.arq.rules.core.FactKeys;
import br.com.arq.rules.core.Rule;
import br.com.arq.rules.core.RuleBuilder;

public final class AgenciaRules {

    public static Rule numeroAgenciaDuplicado(
            AgenciaRepository repository
    ) {

        return RuleBuilder
                .when(
                        "Número da agência já cadastrado",
                        facts -> {

                            String numero =
                                    facts.get(
                                            FactKeys.NUMERO_AGENCIA
                                    );

                            return repository
                                    .findByNumeroAgencia(numero)
                                    .isPresent();
                        }
                )
                .then(
                        facts -> {
                            throw new IllegalArgumentException(
                                    "Agência já cadastrada"
                            );
                        }
                );
    }
}
