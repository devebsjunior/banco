package br.com.arq.asyncecurity.session;


import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
* Serviço responsável por gerar e validar tokens JWT.
*/
@Getter
public class SessionToken {

    private final String uuid;
    private final Long usuarioId;
    private boolean autorizado;
    private final LocalDateTime criadoEm;
    private LocalDateTime expiraEm;
    public SessionToken(Long usuarioId) {
        this.uuid= UUID.randomUUID().toString();
        this.usuarioId = usuarioId;
        this.autorizado = true;
        this.criadoEm = LocalDateTime.now();
        this.expiraEm = criadoEm.plusMinutes(30);
    }
    public void negar() {
        this.autorizado = false;
    }
    public boolean isValido() {
        return autorizado && LocalDateTime.now().isBefore(expiraEm);
    }
}