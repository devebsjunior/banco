package br.com.arq.dto;

import java.math.BigDecimal;

public record ResumoTransacaoDTO(
        BigDecimal total,
        int quantidade
) {}
