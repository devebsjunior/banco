package br.com.arq.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DepositoDTOTest {

    @Test
    @DisplayName("Deve garantir que o Record armazena os dados corretamente")
    void deveValidarCriacaoDoDepositoDto() {
        String numeroEsperado = "123789";
        BigDecimal valorEsperado = new BigDecimal("500.00");
        DepositoDTO dto = new DepositoDTO(numeroEsperado, valorEsperado);
        assertEquals(numeroEsperado, dto.numeroConta(), "O número da conta deve ser igual ao informado");
        assertEquals(valorEsperado, dto.valor(), "O valor do depósito deve ser igual ao informado");
    }
}