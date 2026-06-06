package br.com.arq.dto.response;

public record UsuarioResponseDTO(
        Long id,
        String username,
        String email,
        String primeiroNome,
        String ultimoNome,
        Long clienteId
) {}
