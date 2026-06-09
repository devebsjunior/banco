package br.com.arq.controller;

import br.com.arq.dto.request.UsuarioRequestDTO;
import br.com.arq.model.Usuario;
import br.com.arq.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(
        name = "Usuários",
        description = "Endpoints responsáveis pelo gerenciamento de usuários do sistema"
)
public class UsuarioController {

    private final UsuarioService service;

    @Operation(
            summary = "Criar usuário",
            description = "Cria um novo usuário com perfil e permissões associadas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
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

    @Operation(
            summary = "Listar usuários",
            description = "Retorna todos os usuários cadastrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        return ResponseEntity.ok(service.buscarTodos()
        );
    }

    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados de um usuário específico pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.buscarPorId(id)
        );
    }

    @Operation(
            summary = "Buscar usuário por e-mail",
            description = "Retorna os dados de um usuário com base no e-mail"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(
            @PathVariable String email
    ) {
        return ResponseEntity.ok(
                service.buscarPorEmail(email)
        );
    }

    @Operation(
            summary = "Alterar senha do usuário",
            description = "Atualiza a senha de um usuário com base no ID informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
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