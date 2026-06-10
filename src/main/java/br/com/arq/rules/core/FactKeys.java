package br.com.arq.rules.core;

import br.com.arq.model.Agencia;
import br.com.arq.model.Cliente;
import br.com.arq.model.Conta;
import br.com.arq.model.Usuario;

import java.math.BigDecimal;

/**
 * Classe utilitária responsável por centralizar todas as chaves ({@link FactKey})
 * utilizadas no motor de regras.
  *Evita duplicidade, padroniza o acesso e facilita manutenção.</p>
 */
public class FactKeys {

        public static final FactKey<Conta> CONTA =
                new FactKey<>("CONTA");

        public static final FactKey<Cliente> CLIENTE =
                new FactKey<>("CLIENTE");

        public static final FactKey<Usuario> USUARIO =
                new FactKey<>("USUARIO");

        public static final FactKey<Agencia> AGENCIA =
                new FactKey<>("AGENCIA");

        public static final FactKey<BigDecimal> VALOR =
                new FactKey<>("VALOR");

        public static final FactKey<BigDecimal> SALDO =
                new FactKey<>("SALDO");

        public static final FactKey<String> PERFIL =
                new FactKey<>("PERFIL");

        public static final FactKey<String> CPF =
                new FactKey<>("CPF");

        public static final FactKey<String> EMAIL =
                new FactKey<>("EMAIL");

        public static final FactKey<String> NUMERO_CONTA =
                new FactKey<>("NUMERO_CONTA");

        public static final FactKey<String> NUMERO_AGENCIA =
                new FactKey<>("NUMERO_AGENCIA");
    }
