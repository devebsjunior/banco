package br.com.arq.controller;

import java.util.List;

import br.com.arq.dto.ContaDTO;
import br.com.arq.mapper.ContaMapper;
import br.com.arq.repository.ContaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arq.dto.request.ContaRequestDTO;
import br.com.arq.model.Conta;
import br.com.arq.service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/admin/contas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@Tag( name = "Admin - Contas", description = "Endpoints administrativos para gerenciamento de contas bancárias")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(ContaController.class);
    private final ContaService contaService;
    private final ContaRepository repository;

    @Operation(
            summary = "Criar conta",
            description = "Cria uma nova conta bancária com dados do cliente e agência (somente ADMIN)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou erro de validação"),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado (não é ADMIN)")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ContaDTO> criarConta(@Valid @RequestBody ContaRequestDTO dto) {
        logger.info("Criacao da Conta do Usuario");
        Conta conta =  contaService.criarContaAdmin(dto);
        ContaDTO response = ContaMapper.TO_DTO.apply(conta);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Listar contas",
            description = "Retorna todas as contas cadastradas no sistema (somente ADMIN)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado (não é ADMIN)")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<ContaDTO>> buscarTodas() {
        return ResponseEntity.ok(contaService.buscarTodas());
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Conta>> listarUsuariosComuns() {
        try {
            logger.info("Listando todos os usuários comuns");
            List<Conta> usuarios = repository.findByPerfil("usuario");
            logger.info("Total de usuários comuns encontrados: {}", usuarios.size());
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            logger.error("Erro ao listar usuários comuns", e);
            throw e;
        }
    }


}