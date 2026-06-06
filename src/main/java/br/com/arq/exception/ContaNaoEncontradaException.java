package br.com.arq.exception;

import org.springframework.http.HttpStatus;

public class ContaNaoEncontradaException extends BusinessException {

    public ContaNaoEncontradaException(String numeroConta) {
        super(
                "Conta não encontrada: " + numeroConta,
                "CONTA_NAO_ENCONTRADA",
                HttpStatus.NOT_FOUND
        );
    }
}
