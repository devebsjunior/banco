package br.com.arq.controller;

import java.util.List;

import br.com.arq.dto.ContaDTO;
import br.com.arq.dto.request.OperacaoBancariaDTO;
import br.com.arq.dto.TransacaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arq.dto.TransferenciaDTO;
import br.com.arq.model.Conta;
import br.com.arq.repository.ContaRepository;
import br.com.arq.service.ContaService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/usuarios/contas")
@RequiredArgsConstructor
@Tag(
        name = "Operações Bancárias",
        description = "Operações realizadas em contas bancárias (depósito, saque, transferência e consulta)"
)
public class ContaController {

    private static final Logger logger = LoggerFactory.getLogger(ContaController.class);

    private final ContaService contaService;
    private final ContaRepository repository;

    @Operation(
            summary = "Realizar depósito",
            description = "Adiciona um valor ao saldo da conta informada"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Depósito realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou regra de negócio"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/deposito")
    public ResponseEntity<String> depositar(
            @Valid @RequestBody OperacaoBancariaDTO dto) {
        try {
            logger.info("Iniciando depósito - Conta: {}, Valor: {}", dto.numeroConta(), dto.valor());
            contaService.depositar(dto);
            logger.info("Depósito realizado com sucesso - Conta: {}, Valor: {}", dto.numeroConta(), dto.valor());
            return ResponseEntity.ok(
                    "Depósito de R$ " + dto.valor() + " realizado com sucesso!"
            );
        } catch (RuntimeException e) {
            logger.error("Erro no depósito - Conta: {}, Mensagem: {}", dto.numeroConta(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado no depósito - Conta: {}", dto.numeroConta(), e);
            return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
        }
    }


    @Operation(
            summary = "Realizar saque",
            description = "Realiza o saque se houver saldo disponível na conta"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saque realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Valor inválido ou saldo insuficiente"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/saque")
    public ResponseEntity<?> sacar(
            @Valid @RequestBody OperacaoBancariaDTO dto
    ) {
        try {
            logger.info("Iniciando saque - Conta: {}, Valor: {}", dto.numeroConta(), dto.valor());
            if (dto.valor() == null) {
                logger.warn("Tentativa de saque com valor nulo - Conta: {}", dto.numeroConta());
                return ResponseEntity.badRequest().body("Valor não informado.");
            }
            if (dto.valor().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                logger.warn("Tentativa de saque com valor inválido - Conta: {}, Valor: {}", dto.numeroConta(), dto.valor());
                return ResponseEntity.badRequest().body("Valor de saque inválido.");
            }
            contaService.sacar(dto);
            logger.info("Saque realizado com sucesso - Conta: {}, Valor: {}", dto.numeroConta(), dto.valor());
            return ResponseEntity.ok("Saque realizado com sucesso!");
        } catch (RuntimeException e) {
            logger.error("Erro no saque - Conta: {}, Mensagem: {}", dto.numeroConta(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado no saque - Conta: {}", dto.numeroConta(), e);
            return ResponseEntity.internalServerError().body(
                    "Erro ao processar saque: " + e.getMessage()
            );
        }
    }

    @Operation(
            summary = "Realizar transferência",
            description = "Transfere saldo entre contas de origem e destino"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou saldo insuficiente"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(
            @Valid @RequestBody TransferenciaDTO dto
    ) {
        try {
            logger.info("Iniciando transferência - De: {} para: {}, Valor: {}",
                    dto.contaOrigem(), dto.contaDestino(), dto.valor());
            contaService.transferir(dto);
            logger.info("Transferência realizada com sucesso - De: {} para: {}, Valor: {}",
                    dto.contaOrigem(), dto.contaDestino(), dto.valor());
            return ResponseEntity.ok("Transferência realizada com sucesso");
        } catch (RuntimeException e) {
            logger.error("Erro na transferência - De: {} para: {}, Mensagem: {}",
                    dto.contaOrigem(), dto.contaDestino(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado na transferência - De: {} para: {}",
                    dto.contaOrigem(), dto.contaDestino(), e);
            return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
        }
    }


    @Operation(
            summary = "Consultar extrato",
            description = "Lista todas as transações da conta ordenadas por data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Extrato retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{numero}/extrato")
    public ResponseEntity<List<TransacaoDTO>> verExtrato(
            @PathVariable String numero
    ) {
        try {
            logger.debug("Buscando extrato - Conta: {}", numero);
            List<TransacaoDTO> extrato = contaService.buscarExtrato(numero);
            logger.debug("Extrato localizado - Conta: {}, Total de transações: {}", numero, extrato.size());
            return ResponseEntity.ok(extrato);
        } catch (Exception e) {
            logger.error("Erro ao buscar extrato - Conta: {}", numero, e);
            throw e;
        }
    }


    @Operation(
            summary = "Consultar conta",
            description = "Retorna os dados da conta pelo número"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta encontrada"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{numero}")
    public ResponseEntity<ContaDTO> consultarConta(
            @PathVariable String numero
    ) {
        try {
            logger.debug("Consultando conta - Número: {}", numero);
            ContaDTO conta = contaService.buscarPorNumero(numero);
            logger.debug("Conta localizada - Número: {}, Cliente: {}", numero, conta.nomeCliente());
            return ResponseEntity.ok(conta);
        } catch (Exception e) {
            logger.error("Erro ao consultar conta - Número: {}", numero, e);
            throw e;
        }
    }


}