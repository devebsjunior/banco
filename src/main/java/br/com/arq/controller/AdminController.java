package br.com.arq.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arq.dto.ContaRequestDTO;
import br.com.arq.model.Conta;
import br.com.arq.service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/contas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

	private final ContaService contaService;

	@PostMapping
	public ResponseEntity<?> criarConta(@Valid @RequestBody ContaRequestDTO dto) {
		try {
			Conta contaSalva = contaService.criarConta(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(contaSalva);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<List<Conta>> listarContas() {
		return ResponseEntity.ok(contaService.listarTodas());
	}

}