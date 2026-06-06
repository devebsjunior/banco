package br.com.arq.dto.error;

public record ErrorResponse(
        String code,
        String message,
        int status
) {}
