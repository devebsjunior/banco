package br.com.arq.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;


public record OperacaoBancariaDTO(

        @NotBlank(message = "Banco é obrigatório")
        @Size(min = 3, max = 50, message = "Banco deve ter entre 3 e 50 caracteres")
        String agencia,

        @NotBlank(message = "Agência é obrigatória")
        @Pattern(regexp = "\\d{4}", message = "Agência deve conter exatamente 4 dígitos")
        String numeroAgencia,

        @NotBlank(message = "Banco é obrigatório")
        @Size(min = 3, max = 16, message = "Banco deve ter entre 3 e 50 caracteres")
        String numeroConta,

        BigDecimal valor,

        String logradouro,

        String numero,

        String bairro,

        String cidade,

        String estado,

        String cep


) {}