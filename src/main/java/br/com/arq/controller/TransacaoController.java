package br.com.arq.controller;

import br.com.arq.model.Transacao;
import br.com.arq.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transacoes")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping("/deposito")
    public ResponseEntity<?> depositar(@RequestBody Map<String, Object> payload) {
        try {
            Long contaId = Long.valueOf(payload.get("contaId").toString());
            BigDecimal valor = new BigDecimal(payload.get("valor").toString());
            transacaoService.depositar(contaId, valor);
            return ResponseEntity.ok().body(Map.of("message", "Depósito realizado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/saque")
    public ResponseEntity<?> sacar(@RequestBody Map<String, Object> payload) {
        try {
            Long contaId = Long.valueOf(payload.get("contaId").toString());
            BigDecimal valor = new BigDecimal(payload.get("valor").toString());
            transacaoService.sacar(contaId, valor);
            return ResponseEntity.ok().body(Map.of("message", "Saque realizado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/transferencia")
    public ResponseEntity<?> transferir(@RequestBody Map<String, Object> payload) {
        try {
            Long contaOrigemId = Long.valueOf(payload.get("contaOrigemId").toString());
            String contaDestino = payload.get("contaDestino").toString();
            BigDecimal valor = new BigDecimal(payload.get("valor").toString());

            transacaoService.transferir(contaOrigemId, contaDestino, valor);
            return ResponseEntity.ok().body(Map.of("message", "Transferência realizada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/conta/{contaId}")
    public ResponseEntity<?> listarPorConta(@PathVariable Long contaId) {
        try {
            List<Transacao> extrato = transacaoService.listarExtrato(contaId);
            return ResponseEntity.ok(extrato);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }
}