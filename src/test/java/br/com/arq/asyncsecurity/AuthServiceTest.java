package br.com.arq.asyncsecurity;

import br.com.arq.model.Cliente;
import br.com.arq.model.Conta;
import br.com.arq.repository.ContaRepository;
import br.com.arq.asyncsecurity.security.TokenService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TokenService tokenService;

    private Conta conta;

    @BeforeEach
    void setup() {
        Cliente cliente = new Cliente();
        cliente.setNome("Edson");

        conta = new Conta();
        conta.setNumeroConta("123");
        conta.setSaldo(BigDecimal.valueOf(1000));
        conta.setPerfil("CLIENTE");
        conta.setCliente(cliente);

        // senha = "123456"
        conta.setSenha(org.mindrot.jbcrypt.BCrypt.hashpw("123456", org.mindrot.jbcrypt.BCrypt.gensalt()));
    }


    @Test
    void deveLancarErroQuandoUsuarioNaoExiste() {

        when(contaRepository.findByClienteEmail("email@email.com"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.autenticar("email@email.com", "123456");
        });

        assertEquals("Usuário ou senha inválidos", ex.getMessage());
    }


    @Test
    void deveLancarErroQuandoSenhaIncorreta() {

        when(contaRepository.findByClienteEmail("email@email.com"))
                .thenReturn(Optional.of(conta));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.autenticar("email@email.com", "senhaErrada");
        });

        assertEquals("Senha incorreta!", ex.getMessage());
    }

    @Test
    void deveLancarErroQuandoLoginVazio() {

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.autenticar("", "123456");
        });

        assertEquals("Login não informado", ex.getMessage());
    }


    @Test
    void deveLancarErroQuandoSenhaVazia() {

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.autenticar("email@email.com", "");
        });

        assertEquals("Senha não informada", ex.getMessage());
    }
}