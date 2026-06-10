package br.com.arq.repository;

import br.com.arq.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryTest {

    @Mock
    private UsuarioRepository repository;

    @Test
    void deveBuscarPorEmail() {

        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");

        when(repository.findByEmail("teste@email.com"))
                .thenReturn(Optional.of(usuario));

        Optional<Usuario> result =
                repository.findByEmail("teste@email.com");

        assertTrue(result.isPresent());

        verify(repository).findByEmail("teste@email.com");
    }

    @Test
    void deveVerificarSeEmailExiste() {

        when(repository.existsByEmail("teste@email.com"))
                .thenReturn(true);

        boolean result =
                repository.existsByEmail("teste@email.com");

        assertTrue(result);

        verify(repository).existsByEmail("teste@email.com");
    }
}
