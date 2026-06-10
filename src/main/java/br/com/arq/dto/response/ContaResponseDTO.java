package br.com.arq.dto.response;

import br.com.arq.model.Conta;

import java.math.BigDecimal;


public record ContaResponseDTO(
        Long id,
        String numeroConta,
        String perfil,
        String numeroAgencia,
        String nomeAgencia,
        BigDecimal saldo
) {
    public ContaResponseDTO(Conta conta) {
        this(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getPerfil(),
                conta.getAgencia() != null ? conta.getAgencia().getNumeroAgencia() : "",
                conta.getAgencia() != null ? conta.getAgencia().getNomeAgencia() : "",
                conta.getSaldo()
        );
    }
}

