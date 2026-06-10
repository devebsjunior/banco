package br.com.arq.controller;

import br.com.arq.dto.request.AgenciaRequestDTO;
import br.com.arq.service.AgenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/agencias")
@RequiredArgsConstructor
@Tag( name = "Admin - Agências", description = "Endpoints administrativos para gerenciamento de agências bancárias" )
public class AgenciaController {

    private final AgenciaService agenciaService;

    @Operation(
            summary = "Criar agência",
            description = "Cria uma nova agência bancária (somente ADMIN)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agência criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou erro de validação"),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado (não é ADMIN)")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody AgenciaRequestDTO dto) {
        return ResponseEntity.ok(agenciaService.criar(dto));
    }

    @Operation(
            summary = "Listar agências",
            description = "Retorna a listagem de todas as agências (somente ADMIN)"
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<?> listarTodas() {
        return ResponseEntity.ok(agenciaService.buscarTodas());
    }
}

