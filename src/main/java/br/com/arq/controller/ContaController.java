package br.com.arq.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import br.com.arq.dto.ContaDTO;
import br.com.arq.dto.OperacaoBancariaDTO;
import br.com.arq.dto.TransacaoDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arq.dto.TransferenciaDTO;
import br.com.arq.model.Conta;
import br.com.arq.model.Transacao;
import br.com.arq.repository.ContaRepository;
import br.com.arq.service.ContaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios/contas")
@RequiredArgsConstructor
public class ContaController {

		private final ContaService contaService;
		private final ContaRepository repository;

		@PostMapping("/deposito")
		public ResponseEntity<String> depositar(
		 @Valid @RequestBody OperacaoBancariaDTO dto ) {
			try {
				contaService.depositar(dto);

				return ResponseEntity.ok(
			 	"Depósito de R$ " + dto.valor() + " realizado com sucesso!"
				);

			} catch (RuntimeException e) {
				return ResponseEntity.badRequest().body(e.getMessage());

			} catch (Exception e) {
				return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
			}
		}


		@PostMapping("/saque")
		public ResponseEntity<?> sacar(
				@Valid @RequestBody OperacaoBancariaDTO dto
		) {
			try {

				if (dto.valor().compareTo(null) == 0) {
					return ResponseEntity.badRequest().body("Valor não informado.");
				}

				if (dto.valor().compareTo(java.math.BigDecimal.ZERO) <= 0) {
					return ResponseEntity.badRequest().body("Valor de saque inválido.");
				}
				contaService.sacar(dto);
				return ResponseEntity.ok("Saque realizado com sucesso!");
			} catch (RuntimeException e) {
				return ResponseEntity.badRequest().body(e.getMessage());

			} catch (Exception e) {
				return ResponseEntity.internalServerError().body(
						"Erro ao processar saque: " + e.getMessage()
				);
			}
		}

		@PostMapping("/transferir")
		public ResponseEntity<?> transferir(
		 @Valid @RequestBody TransferenciaDTO dto
		) {
			try {
				System.out.println(
						"Recebendo transferência: "
								+ dto.contaOrigem() + " para " + dto.contaDestino()
				);

				contaService.transferir(dto);
				return ResponseEntity.ok("Transferência realizada com sucesso");

			} catch (RuntimeException e) {
				return ResponseEntity.badRequest().body(e.getMessage());

			} catch (Exception e) {
				return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
			}
		}


		@GetMapping("/{numero}/extrato")
		public ResponseEntity<List<TransacaoDTO>> verExtrato(
				@PathVariable String numero
		) {
			List<TransacaoDTO> extrato = contaService.buscarExtrato(numero);
			return ResponseEntity.ok(extrato);
		}


		@GetMapping("/{numero}")
		public ResponseEntity<ContaDTO> consultarConta(
				@PathVariable String numero
		) {
			return ResponseEntity.ok(contaService.buscarPorNumero(numero));
		}

		@GetMapping("/admin/usuarios")
		public ResponseEntity<List<Conta>> listarUsuariosComuns() {
			List<Conta> usuarios = repository.findByPerfil("usuario");
			return ResponseEntity.ok(usuarios);
		}
 }