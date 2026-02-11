package br.com.arq.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContaRequestDTO(
		@NotBlank(message = "Nome é obrigatório") String nome,
	    @NotBlank(message = "CPF é obrigatório") String cpf,
	    @NotBlank(message = "Email é obrigatório") @Email String email,
	    @NotBlank(message = "Número da conta é obrigatório") String numeroConta,
	    @NotNull @Min(0) BigDecimal saldo,
	    @NotBlank String perfil,
	    @NotBlank String senha
	) {}