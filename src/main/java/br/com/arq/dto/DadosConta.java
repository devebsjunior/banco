package br.com.arq.dto;

import java.math.BigDecimal;

public record DadosConta(String numeroConta, String perfil, String senha, BigDecimal saldo) {}
