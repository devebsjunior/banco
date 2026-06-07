package br.com.arq.dto.request;


import br.com.arq.enums.TipoPerfil;
import br.com.arq.model.Perfil;

public record UsuarioRequestDTO(

          String username,
          String email,
          String senha,
          String primeiroNome,
          String ultimoNome,
          Perfil perfil

) {}
