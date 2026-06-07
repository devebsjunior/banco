package br.com.arq.dto;

import java.math.BigDecimal;

public record TransferenciaDTO(
		String bancoOrigem,
		String agenciaOrigem,
		String contaOrigem,
		String bancoDestino,
		String agenciaDestino,
		String contaDestino,
		BigDecimal valor
) {}