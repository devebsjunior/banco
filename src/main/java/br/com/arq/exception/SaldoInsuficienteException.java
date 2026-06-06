package br.com.arq.exception;

import org.springframework.http.HttpStatus;

public class    SaldoInsuficienteException extends BusinessException {

         public SaldoInsuficienteException() {
            super(
                    "Saldo insuficiente",
                    "SALDO_INSUFICIENTE",
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
    }