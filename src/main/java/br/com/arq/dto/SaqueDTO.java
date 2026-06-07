package br.com.arq.dto;

import java.math.BigDecimal;

public record SaqueDTO(
        String banco,
        String agencia,
        String numeroConta,
        BigDecimal valor
) {}
