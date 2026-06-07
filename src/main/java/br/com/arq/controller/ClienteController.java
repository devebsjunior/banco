package br.com.arq.controller;


import br.com.arq.dto.request.ClienteRequestDTO;
import br.com.arq.model.Cliente;
import br.com.arq.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

  private final ClienteService service;

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

  @GetMapping
  public ResponseEntity<List<Cliente>> buscarTodos() {
    return ResponseEntity.ok(service.buscarTodos());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(service.buscarPorId(id));
    }
    catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
  }

  @GetMapping("/cpf/{cpf}")
  public ResponseEntity<?> buscarPorCpf(@PathVariable String cpf) {
    try {
      return ResponseEntity.ok(service.buscarPorCpf(cpf));
    }
    catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
  }

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