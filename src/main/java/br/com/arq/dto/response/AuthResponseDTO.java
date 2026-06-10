package br.com.arq.dto.response;

import java.math.BigDecimal;

public record AuthResponseDTO(
        Long id,
        String nome,
        String numeroConta,
        String perfil,
        String token,
        BigDecimal saldo
) {}
