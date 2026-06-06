package br.com.arq.dto.request;


public record UsuarioRequestDTO(

          String username,
          String email,
          String senha,
          String primeiroNome,
          String ultimoNome,
          Long clienteId

) {}
