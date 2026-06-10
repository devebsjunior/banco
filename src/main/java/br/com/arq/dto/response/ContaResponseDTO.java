package br.com.arq.dto.response;

import br.com.arq.model.Conta;


public record ContaResponseDTO(
        Long id,
        String numeroConta,
        String perfil
) {
    public ContaResponseDTO(Conta conta) {
        this(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getPerfil()
        );
    }
}