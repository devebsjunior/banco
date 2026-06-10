package br.com.arq.model;

import static org.junit.jupiter.api.Assertions.*;

import br.com.arq.enums.TipoTransacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

class TransacaoTest {

        @Test
        void deveCriarTransacao() {

            Conta conta = new Conta();
            conta.setNumeroConta("123");

            Transacao t = Transacao.builder()
                    .id(1L)
                    .numeroConta("123")
                    .conta(conta)
                    .tipo(TipoTransacao.DEPOSITO)
                    .valor(BigDecimal.valueOf(100))
                    .build();

            assertNotNull(t);
            assertEquals("123", t.getNumeroConta());
            assertEquals(TipoTransacao.DEPOSITO, t.getTipo());
            assertEquals(BigDecimal.valueOf(100), t.getValor());
        }

        @Test
        void deveGerarDataAoPersistir() {

            Transacao t = new Transacao();

            t.onCreate(); // simula @PrePersist

            assertNotNull(t.getDataHora());
        }
}
