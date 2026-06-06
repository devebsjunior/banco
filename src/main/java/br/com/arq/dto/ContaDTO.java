package br.com.arq.dto;

import java.math.BigDecimal;

public record ContaDTO(
        String numeroConta,
        BigDecimal saldo,
        String perfil,
        String nomeCliente
) {}