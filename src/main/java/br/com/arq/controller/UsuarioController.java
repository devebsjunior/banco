package br.com.arq.controller;



import br.com.arq.dto.request.UsuarioRequestDTO;
import br.com.arq.model.Usuario;
import br.com.arq.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

  private final UsuarioService service;

  @PostMapping
  public ResponseEntity<Usuario> criar(
          @Valid @RequestBody UsuarioRequestDTO dto
                                      ) {

    Usuario usuario =
            service.criarUsuario(
                    dto
                                );

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(usuario);
  }

  @GetMapping
  public ResponseEntity<List<Usuario>> buscarTodos() {

    return ResponseEntity.ok(
            service.buscarTodos()
                            );
  }

  @GetMapping("/{id}")
  public ResponseEntity<Usuario> buscarPorId(
          @PathVariable Long id
                                            ) {

    return ResponseEntity.ok(
            service.buscarPorId(id)
                            );
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<Usuario> buscarPorEmail(
          @PathVariable String email
                                               ) {

    return ResponseEntity.ok(
            service.buscarPorEmail(email)
                            );
  }

  @PutMapping("/{id}/senha")
  public ResponseEntity<Usuario> alterarSenha(
          @PathVariable Long id,
          @RequestParam String senha
                                             ) {

    return ResponseEntity.ok(
            service.alterarSenha(
                    id,
                    senha
                                )
                            );
  }
}