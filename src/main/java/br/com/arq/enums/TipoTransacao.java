package br.com.arq.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoTransacao {

    CRIAR_CONTA("Criar conta correntista"),
    DEPOSITO("Depósito realizado"),
    SAQUE("Saque realizado"),
    TRANSFERENCIA("Transferência realizada"),
    RULE_EXECUTION("Execução de regra");

    private final String mensagem;
}