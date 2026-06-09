package br.com.arq.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDTO(

        @NotBlank
        String nome,
        @NotBlank
        String cpf,
        @NotBlank
        @Email
        String email

) {
}