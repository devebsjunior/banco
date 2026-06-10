package br.com.arq.repository;

import br.com.arq.model.Transacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoRepositoryTest {

    @Mock
    private TransacaoRepository repository;

    @Test
    void deveBuscarTransacoesPorNumeroConta() {

        // cenário
        Transacao t = new Transacao();
        t.setNumeroConta("123");

        when(repository.findByNumeroContaOrderByDataHoraDesc("123"))
                .thenReturn(List.of(t));

        // execução
        List<Transacao> result =
                repository.findByNumeroContaOrderByDataHoraDesc("123");

        // validação
        assertFalse(result.isEmpty());
        assertEquals("123", result.get(0).getNumeroConta());

        verify(repository)
                .findByNumeroContaOrderByDataHoraDesc("123");
    }
}