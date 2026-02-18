package br.com.arq.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SaqueDTOTest {

    @Test
    @DisplayName("Deve garantir que o Record de Saque armazena os dados corretamente")
    void deveValidarDadosDeSaque() {
        String numeroEsperado = "123789";
        BigDecimal valorEsperado = new BigDecimal("150.00");
        SaqueDTO dto = new SaqueDTO(numeroEsperado, valorEsperado);
        assertEquals(numeroEsperado, dto.numeroConta(), "O número da conta deve ser o mesmo informado");
        assertEquals(valorEsperado, dto.valor(), "O valor do saque deve ser o mesmo informado");
    }
}