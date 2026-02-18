package br.com.arq.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
public class UsuarioController {

	private final ContaService contaService;
	private final  ContaRepository repository;

	@PostMapping("/{numero}/deposito")
	public ResponseEntity<String> depositar(@PathVariable String numero, @RequestBody Map<String, BigDecimal> request) {
		try {
			BigDecimal valor = new BigDecimal(request.get("valor").toString());
			contaService.depositar(numero, valor);

			return ResponseEntity.ok("Depósito de R$ " + valor + " realizado com sucesso!");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
		}
	}

	@PostMapping("/{numero}/saque")
	public ResponseEntity<?> sacar(@PathVariable String numero, @RequestBody Map<String, Object> request) {
		try {
			Object valorObj = request.get("valor");
			if (valorObj == null) {
				return ResponseEntity.badRequest().body("Valor não informado.");
			}

			BigDecimal valor = new BigDecimal(valorObj.toString());

			if (valor.compareTo(BigDecimal.ZERO) <= 0) {
				return ResponseEntity.badRequest().body("Valor de saque inválido.");
			}

			contaService.sacar(numero, valor);

			return ResponseEntity.ok().body("Saque realizado com sucesso!");

		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Erro ao processar saque: " + e.getMessage());
		}
	}

	@PostMapping("/transferir")
	public ResponseEntity<?> transferir(@RequestBody TransferenciaDTO dto) {
	    try {
	        System.out.println("Recebendo transferência: " + dto.origem() + " para " + dto.destino());
	        contaService.transferir(dto); 
	        return ResponseEntity.ok().body("Transferência realizada com sucesso");
	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
	    }
	}
	
	@GetMapping("/{numero}/extrato")
	public ResponseEntity<List<Transacao>> verExtrato(@PathVariable String numero) {
		List<Transacao> extrato = contaService.buscarExtrato(numero);
		return ResponseEntity.ok(extrato);
	}

	@GetMapping("/{numero}")
	public ResponseEntity<Conta> consultarConta(@PathVariable String numero) {
		return ResponseEntity.ok(contaService.listarTodas().stream().filter(c -> c.getNumeroConta().equals(numero))
				.findFirst().orElseThrow(() -> new RuntimeException("Conta nao encontrada")));
	}

	@GetMapping("/admin/usuarios")
	public ResponseEntity<List<Conta>> listarUsuariosComuns() {
		List<Conta> usuarios = repository.findByPerfil("usuario");
		return ResponseEntity.ok(usuarios);
	}
}