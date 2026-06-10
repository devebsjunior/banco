package br.com.arq.repository;

import br.com.arq.model.Agencia;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgenciaRepositoryTest {

    @Mock
    private AgenciaRepository repository;

    @Test
    void deveBuscarPorNumeroAgencia() {

        Agencia agencia = new Agencia();
        agencia.setNumeroAgencia("0001");

        when(repository.findByNumeroAgencia("0001"))
                .thenReturn(Optional.of(agencia));

        Optional<Agencia> result =
                repository.findByNumeroAgencia("0001");

        assertTrue(result.isPresent());
        assertEquals("0001", result.get().getNumeroAgencia());

        verify(repository).findByNumeroAgencia("0001");
    }
}
