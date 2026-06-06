package br.com.arq.exception;

import org.springframework.http.HttpStatus;

public class ContaSemClienteException extends BusinessException {

    public ContaSemClienteException(String numeroConta) {
        super(
                "Conta sem cliente vinculada: " + numeroConta,
                "CONTA_SEM_CLIENTE",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}