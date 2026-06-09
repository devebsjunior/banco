package br.com.arq.rules.core.catalog;

import br.com.arq.dto.request.UsuarioRequestDTO;
import br.com.arq.rules.core.Rule;
import br.com.arq.rules.core.RuleBuilder;


public final class UsuarioRules {

    private UsuarioRules() {
    }

    public static Rule emailObrigatorio() {

        return RuleBuilder
                .when(
                        "Email obrigatório",
                        facts -> {

                            UsuarioRequestDTO dto =
                                    facts.get(
                                            UsuarioRequestDTO.class
                                    );

                            return dto.email() == null
                                    || dto.email().isBlank();
                        }
                )
                .then(
                        facts -> {
                            throw new IllegalArgumentException(
                                    "Email obrigatório"
                            );
                        }
                );
    }

    public static Rule senhaObrigatoria() {

        return RuleBuilder
                .when(
                        "Senha obrigatória",
                        facts -> {

                            UsuarioRequestDTO dto =
                                    facts.get(
                                            UsuarioRequestDTO.class
                                    );

                            return dto.senha() == null
                                    || dto.senha().isBlank();
                        }
                )
                .then(
                        facts -> {
                            throw new IllegalArgumentException(
                                    "Senha obrigatória"
                            );
                        }
                );
    }
}
