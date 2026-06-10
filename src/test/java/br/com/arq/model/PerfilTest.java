package br.com.arq.model;


import br.com.arq.enums.TipoPerfil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerfilTest {

    @Test
    void deveCriarPerfilComBuilder() {

        Perfil perfil = Perfil.builder()
                .id(1L)
                .tipoPerfil(TipoPerfil.ADMIN)
                .nivelPermissao(1)
                .build();

        assertNotNull(perfil);
        assertEquals(TipoPerfil.ADMIN, perfil.getTipoPerfil());
        assertEquals(1, perfil.getNivelPermissao());
    }

    @Test
    void deveCriarPerfilComConstrutor() {

        Perfil perfil = new Perfil(1L, TipoPerfil.USER, 2);

        assertEquals(TipoPerfil.USER, perfil.getTipoPerfil());
        assertEquals(2, perfil.getNivelPermissao());
    }
}
