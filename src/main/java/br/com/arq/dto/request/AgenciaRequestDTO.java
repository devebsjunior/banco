package br.com.arq.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AgenciaRequestDTO(

        @NotBlank(message = "Nome da agência é obrigatório")
        String nomeAgencia,

        @NotBlank(message = "Número da agência é obrigatório")
        String numeroAgencia,

        @NotBlank(message = "Logradouro é obrigatório")
        String logradouro,

        @NotBlank(message = "Número é obrigatório")
        String numero,

        @NotBlank(message = "Bairro é obrigatório")
        String bairro,

        @NotBlank(message = "Cidade é obrigatória")
        String cidade,

        @NotBlank(message = "Estado é obrigatório")
        @Size(
                min = 2,
                max = 2,
                message = "Estado deve possuir 2 caracteres"
        )
        String estado,

        @NotBlank(message = "CEP é obrigatório")
        @Pattern(
                regexp = "\\d{5}-?\\d{3}",
                message = "CEP inválido"
        )
        String cep

) {
}