package br.com.arq.mapper;

import br.com.arq.dto.TransacaoDTO;
import br.com.arq.model.Transacao;

import java.util.function.Function;

public class TransacaoMapper {


        public static final Function<Transacao, TransacaoDTO> TO_DTO =
                t -> new TransacaoDTO(
                        t.getId(),
                        t.getTipo(),
                        t.getValor(),
                        t.getDataHora()
                );

}




