package br.com.arq.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContaRequestDTO(
	  	@NotBlank(message = "Nome é obrigatório") String nome,
	    @NotBlank(message = "CPF é obrigatório") String cpf,
	    @NotBlank(message = "Email é obrigatório") @Email String email,
   		@NotBlank(message ="nome da Agencia") String nomeAgencia,
  		@NotBlank(message ="codigo da Agencia") String codigoAgencia,
	    @NotBlank(message ="cep") String cep,
		  @NotBlank(message = "Número da conta é obrigatório") String numeroConta,

			@NotNull @Min(0) BigDecimal saldo,
	    @NotBlank String senha,
		  String logradouro,
		  String numero,
		  String bairro,
		  String cidade,
		  String estado
) {}