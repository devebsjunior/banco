package br.com.arq.rules.core;

import java.math.BigDecimal;
import br.com.arq.model.Conta;

public final class FactKeys {

        private FactKeys() {
        }

        public static final FactKey<Conta> CONTA =
                new FactKey<>("CONTA");

        public static final FactKey<BigDecimal> VALOR =
                new FactKey<>("VALOR");

        public static final FactKey<BigDecimal> SALDO =
                new FactKey<>("SALDO");

        public static final FactKey<String> PERFIL =
                new FactKey<>("PERFIL");

        public static final FactKey<String> BANCO =
                new FactKey<>("BANCO");

        public static final FactKey<String> AGENCIA =
                new FactKey<>("AGENCIA");
   }

