//package br.com.arq.auth;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import br.com.arq.model.Conta;
//import br.com.arq.repository.ContaRepository;
//
//@ExtendWith(MockitoExtension.class)
//class AuthServiceTest {
//
//    @Mock
//    private ContaRepository contaRepository;
//
//    @InjectMocks
//    private AuthService authService;
//
//    @Test
//    @DisplayName("Deve autenticar com sucesso quando login e senha estiverem corretos")
//    void deveAutenticarComSucesso() {
//        String login = "123789";
//        String senha = "password123";
//        Conta contaMock = new Conta();
//        contaMock.setNumeroConta(login);
//        contaMock.setSenha(senha);
//
//        when(contaRepository.findByNumeroConta(login)).thenReturn(Optional.of(contaMock));
//
//        Conta resultado = authService.autenticar(login, senha);
//        assertNotNull(resultado);
//        assertEquals(login, resultado.getNumeroConta());
//        verify(contaRepository, times(1)).findByNumeroConta(login);
//    }
//
//    @Test
//    @DisplayName("Deve lançar exceção quando a senha estiver incorreta")
//    void deveLancarExcecaoSenhaIncorreta() {
//        String login = "123789";
//        Conta contaMock = new Conta();
//        contaMock.setNumeroConta(login);
//        contaMock.setSenha("senha_correta");
//
//        when(contaRepository.findByNumeroConta(login)).thenReturn(Optional.of(contaMock));
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            authService.autenticar(login, "senha_errada");
//        });
//        assertEquals("Senha incorreta!", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Deve lançar exceção quando a conta não for encontrada")
//    void deveLancarExcecaoContaInexistente() {
//        when(contaRepository.findByNumeroConta("999999")).thenReturn(Optional.empty());
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            authService.autenticar("999999", "123456");
//        });
//        assertTrue(exception.getMessage().contains("Conta não encontrada"));
//    }
//}