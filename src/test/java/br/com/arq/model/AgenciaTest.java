package br.com.arq.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgenciaTest {

    @Test
    void deveCriarAgenciaComBuilder() {

        Agencia agencia = Agencia.builder()
                .id(1L)
                .codigo("001")
                .nomeAgencia("Banco Teste")
                .numeroAgencia("0001")
                .cidade("São Paulo")
                .build();

        assertNotNull(agencia);
        assertEquals("Banco Teste", agencia.getNomeAgencia());
        assertEquals("0001", agencia.getNumeroAgencia());
    }
}
