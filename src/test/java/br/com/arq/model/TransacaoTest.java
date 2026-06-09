package br.com.arq.model;

import static org.junit.jupiter.api.Assertions.*;

import br.com.arq.enums.TipoTransacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

class TransacaoTest {


        @Test
        void deveTestarBuilderETipos() {

            String numeroConta = "123789";

            Transacao transacao = Transacao.builder()
                    .numeroConta(numeroConta)
                    .tipo(TipoTransacao.DEPOSITO)
                    .valor(new BigDecimal("500.00"))
                    .build();


            assertEquals(numeroConta, transacao.getNumeroConta());


            assertEquals(TipoTransacao.DEPOSITO, transacao.getTipo());

            assertEquals(new BigDecimal("500.00"), transacao.getValor());
        }
 }
