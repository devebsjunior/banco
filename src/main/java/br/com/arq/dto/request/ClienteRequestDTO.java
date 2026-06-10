package br.com.arq.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        String cpf,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        String senha,

        @NotNull(message = "Os dados do endereço são obrigatórios")
        @Valid
        EnderecoClienteDTO enderecoCliente,

        @NotNull(message = "Os dados da conta bancária são obrigatórios")
        @Valid
        ContaClienteDTO conta

) {
        public record EnderecoClienteDTO(
                @NotBlank String logradouro,
                @NotBlank String numero,
                String complemento,
                @NotBlank String bairro,
                @NotBlank String cidade,
                @NotBlank String estado,
                @NotBlank String cep
        ) {}

        public record ContaClienteDTO(
                @NotBlank String numeroConta,
                @NotNull AgenciaIdDTO agencia
        ) {}

        public record AgenciaIdDTO(
                @NotNull Long id
        ) {}
}