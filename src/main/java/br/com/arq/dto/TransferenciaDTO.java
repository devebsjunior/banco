package br.com.arq.dto;

import java.math.BigDecimal;

public record TransferenciaDTO(
		String origem, 
		String destino, 
		BigDecimal valor) {}