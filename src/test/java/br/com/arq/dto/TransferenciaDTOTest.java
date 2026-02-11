package br.com.arq.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransferenciaDTOTest {

    @Test
    @DisplayName("Deve garantir que o Record de Transferência armazena os dados de origem, destino e valor corretamente")
    void deveValidarDadosDeTransferencia() {
        String contaOrigem = "123789";
        String contaDestino = "987654";
        BigDecimal valorTransferencia = new BigDecimal("250.00");
        TransferenciaDTO dto = new TransferenciaDTO(contaOrigem, contaDestino, valorTransferencia);
        assertEquals(contaOrigem, dto.origem(), "A conta de origem deve ser a mesma informada");
        assertEquals(contaDestino, dto.destino(), "A conta de destino deve ser a mesma informada");
        assertEquals(valorTransferencia, dto.valor(), "O valor da transferência deve ser o mesmo informado");
    }
}