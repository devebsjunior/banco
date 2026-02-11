package br.com.arq.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginRequestDTOTest {

    @Test
    @DisplayName("Deve garantir que o Record de Login armazena os dados corretamente")
    void deveValidarDadosDeLogin() {
        String loginEsperado = "devedsonbelem@gmail.com";
        String senhaEsperada = "123456";
        LoginRequestDTO dto = new LoginRequestDTO(loginEsperado, senhaEsperada);
        assertEquals(loginEsperado, dto.login(), "O login deve ser igual ao informado");
        assertEquals(senhaEsperada, dto.senha(), "A senha deve ser igual à informada");
    }
}