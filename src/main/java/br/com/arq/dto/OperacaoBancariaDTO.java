package br.com.arq.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;


public record OperacaoBancariaDTO(

        @NotBlank(message = "Banco é obrigatório")
        @Size(min = 3, max = 50, message = "Banco deve ter entre 3 e 50 caracteres")
        String banco,

        @NotBlank(message = "Agência é obrigatória")
        @Pattern(regexp = "\\d{4}", message = "Agência deve conter exatamente 4 dígitos")
        String agencia,

        @NotBlank(message = "Número da conta é obrigatório")
        @Pattern(regexp = "\\d{5,10}", message = "Número da conta deve ter entre 5 e 10 dígitos")
        String numeroConta,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        @Digits(integer = 10, fraction = 2, message = "Valor inválido")
        BigDecimal valor

) {}