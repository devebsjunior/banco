package br.com.arq.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransferenciaDTOTest {

    @Test
    @DisplayName("Deve garantir que o Record de Transferência armazena os dados corretamente")
    void deveValidarDadosDeTransferencia() {

        String bancoOrigem = "001";
        String agenciaOrigem = "1234";
        String contaOrigem = "123789";

        String bancoDestino = "002";
        String agenciaDestino = "5678";
        String contaDestino = "987654";

        BigDecimal valorTransferencia = new BigDecimal("250.00");

        TransferenciaDTO dto = new TransferenciaDTO(
                bancoOrigem,
                agenciaOrigem,
                contaOrigem,
                bancoDestino,
                agenciaDestino,
                contaDestino,
                valorTransferencia
        );

        assertEquals(contaOrigem, dto.contaOrigem());
        assertEquals(contaDestino, dto.contaDestino());
        assertEquals(valorTransferencia, dto.valor());
    }
}