package br.com.arq.controller;


import br.com.arq.dto.request.ClienteRequestDTO;
import br.com.arq.dto.response.ClienteResponseDTO;
import br.com.arq.model.Cliente;
import br.com.arq.service.ClienteService;
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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag( name = "Clientes",  description = "Endpoints responsáveis pelo gerenciamento de clientes")

public class ClienteController {

  private final ClienteService service;

  @Operation(
          summary = "Criar cliente",
          description = "Cria um novo cliente no sistema"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
          @ApiResponse(responseCode = "400", description = "Erro de validação")
  })
  @PostMapping
  public ResponseEntity<?> criar(@Valid @RequestBody ClienteRequestDTO dto) {
    try {
      Cliente cliente = service.criar(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }
    catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @Operation(
          summary = "Listar clientes",
          description = "Retorna todos os clientes cadastrados"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
  })
  @GetMapping
  public ResponseEntity<List<ClienteResponseDTO>> buscarTodos() {
    return ResponseEntity.ok(
            service.buscarTodos().stream()
                    .map(ClienteResponseDTO::new)
                    .toList()
    );
  }

  @Operation(
          summary = "Buscar cliente por ID",
          description = "Retorna um cliente específico pelo ID"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
          @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(service.buscarPorId(id));
    }
    catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
  }

  @Operation(
          summary = "Buscar cliente por CPF",
          description = "Retorna um cliente com base no CPF"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
          @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
  })
  @GetMapping("/cpf/{cpf}")
  public ResponseEntity<?> buscarPorCpf(@PathVariable String cpf) {
    try {
      return ResponseEntity.ok(service.buscarPorCpf(cpf));
    }
    catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
  }

  @Operation(
          summary = "Excluir cliente",
          description = "Remove um cliente do sistema pelo ID"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
          @ApiResponse(responseCode = "400", description = "Erro ao excluir cliente")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<?> excluir(@PathVariable Long id) {
    try {
      service.excluir(id);
      return ResponseEntity.noContent().build();
    }
    catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}