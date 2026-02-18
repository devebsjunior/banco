package br.com.arq.dto;

import java.math.BigDecimal;

public record DepositoDTO(String numeroConta, BigDecimal valor) {
}