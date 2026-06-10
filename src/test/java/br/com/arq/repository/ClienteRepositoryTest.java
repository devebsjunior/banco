package br.com.arq.repository;

import br.com.arq.model.Agencia;
import br.com.arq.model.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteRepositoryTest {

    @Mock
    private ClienteRepository repository;

    @Test
    void deveBuscarPorCpf() {

        Cliente cliente = new Cliente();
        cliente.setCpf("12345678900");

        when(repository.findByCpf("12345678900"))
                .thenReturn(Optional.of(cliente));

        Optional<Cliente> result =
                repository.findByCpf("12345678900");

        assertTrue(result.isPresent());

        verify(repository).findByCpf("12345678900");
    }

    @Test
    void deveBuscarTodosComContas() {

        Cliente cliente = new Cliente();

        when(repository.findAllComContas())
                .thenReturn(List.of(cliente));

        List<Cliente> result =
                repository.findAllComContas();

        assertFalse(result.isEmpty());

        verify(repository).findAllComContas();
    }
}