package br.com.arq.utils;



import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilsTest {

    @Test
    void deveRetornarDataHoraNoTimezoneSaoPaulo() {
        // quando
        LocalDateTime resultado = TimeUtils.now();

        // então
        assertNotNull(resultado);

        // valida comparando com a mesma zona
        LocalDateTime esperado =
                LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

        // diferença aceitável de alguns segundos
        assertTrue(
                Math.abs(resultado.getSecond() - esperado.getSecond()) <= 5,
                "A diferença entre os horários deve ser pequena"
        );
    }
}