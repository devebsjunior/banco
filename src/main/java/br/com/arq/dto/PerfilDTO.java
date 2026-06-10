package br.com.arq.dto;


import br.com.arq.enums.TipoPerfil;

public record PerfilDTO(
        Long id,
        TipoPerfil tipoPerfil,
        Integer nivelPermissao
) {
}

