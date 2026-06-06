package br.com.arq.exception;

import br.com.arq.dto.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handler global de exceções da aplicação.
 *
 * <p>Centraliza o tratamento de erros e padroniza as respostas da API REST,
 * evitando duplicação de código nos controllers.</p>
 *
 * <p>Mapeia exceções específicas para seus respectivos códigos HTTP.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de negócio.
     *
     * <p>Utilizado para regras de domínio, como:</p>
     * <ul>
     *     <li>Saldo insuficiente</li>
     *     <li>Valor inválido</li>
     * </ul>
     *
     * @param ex exceção de negócio lançada pela aplicação
     * @return resposta com status HTTP 422 (UNPROCESSABLE_ENTITY)
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(
                        ex.getCode(),
                        ex.getMessage(),
                        HttpStatus.UNPROCESSABLE_ENTITY.value()
                ));
    }

    /**
     * Trata exceções de recurso não encontrado.
     *
     * <p>Exemplo: conta inexistente.</p>
     *
     * @param ex exceção de conta não encontrada
     * @return resposta com status HTTP 404 (NOT_FOUND)
     */
    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ContaNaoEncontradaException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ex.getCode(),
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value()
                ));
    }

    /**
     * Trata erros de requisição inválida.
     *
     * <p>Exemplo: parâmetros incorretos ou argumentos inválidos.</p>
     *
     * @param ex exceção de argumento inválido
     * @return resposta com status HTTP 400 (BAD_REQUEST)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "BAD_REQUEST",
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value()
                ));
    }

    /**
     * Trata exceções não mapeadas.
     *
     * <p>Handler de fallback para capturar erros inesperados da aplicação.
     * Evita exposição de detalhes internos ao cliente.</p>
     *
     * @param ex exceção genérica
     * @return resposta com status HTTP 500 (INTERNAL_SERVER_ERROR)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "INTERNAL_ERROR",
                        "Erro interno no servidor",
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }
}