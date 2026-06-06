package br.com.arq.dto;

import br.com.arq.enums.TipoTransacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoExtratoDTO(
        TipoTransacao tipo,
        BigDecimal valor,
        LocalDateTime dataHora
) {}
