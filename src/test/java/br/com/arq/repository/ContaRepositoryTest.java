package br.com.arq.repository;

import br.com.arq.model.Agencia;
import br.com.arq.model.Cliente;
import br.com.arq.model.Conta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaRepositoryTest {

    @Mock
    private ContaRepository contaRepository;

    private Cliente cliente;

    private Agencia agencia;

    private Conta conta;


    @BeforeEach
    void setUp() {

        cliente = new Cliente();
        cliente.setNome("Teste");
        cliente.setCpf("12345678900");
        cliente.setEmail("teste@email.com");

        agencia = new Agencia();
        agencia.setNomeAgencia("Banco Teste");
        agencia.setNumeroAgencia("0001");
        agencia.setCodigo("001");

        conta = new Conta();
        conta.setNumeroConta("111111");
        conta.setSaldo(BigDecimal.ZERO);
        conta.setCliente(cliente);
        conta.setPerfil("usuario");
        conta.setAgencia(agencia);
    }


    @Test
    void deveEncontrarContaPorNumero() {

        when(contaRepository.findByNumeroConta("111111"))
                .thenReturn(Optional.of(conta));

        Optional<Conta> resultado =
                contaRepository.findByNumeroConta("111111");

        assertTrue(resultado.isPresent());
        assertEquals("111111", resultado.get().getNumeroConta());

        verify(contaRepository).findByNumeroConta("111111");
    }


    @Test
    void deveEncontrarComLockPessimista() {

        when(contaRepository.findByNumeroContaWithLock("111111"))
                .thenReturn(Optional.of(conta));

        Optional<Conta> resultado =
                contaRepository.findByNumeroContaWithLock("111111");

        assertTrue(resultado.isPresent());
        assertEquals("111111", resultado.get().getNumeroConta());

        verify(contaRepository).findByNumeroContaWithLock("111111");
    }


    @Test
    void deveEncontrarPorEmail() {

        when(contaRepository.findByClienteEmail("teste@email.com"))
                .thenReturn(Optional.of(conta));

        Optional<Conta> resultado =
                contaRepository.findByClienteEmail("teste@email.com");

        assertTrue(resultado.isPresent());

        verify(contaRepository).findByClienteEmail("teste@email.com");
    }


    @Test
    void deveEncontrarPorEmailOuCpf() {

        when(contaRepository.findByClienteEmailOrClienteCpf("teste@email.com", "000"))
                .thenReturn(Optional.of(conta));

        when(contaRepository.findByClienteEmailOrClienteCpf("x@email.com", "12345678900"))
                .thenReturn(Optional.of(conta));

        Optional<Conta> porEmail =
                contaRepository.findByClienteEmailOrClienteCpf("teste@email.com", "000");

        Optional<Conta> porCpf =
                contaRepository.findByClienteEmailOrClienteCpf("x@email.com", "12345678900");

        assertTrue(porEmail.isPresent());
        assertTrue(porCpf.isPresent());

        verify(contaRepository, times(2))
                .findByClienteEmailOrClienteCpf(any(), any());
    }

    @Test
    void deveEncontrarPorPerfil() {

        when(contaRepository.findByPerfil("usuario"))
                .thenReturn(List.of(conta));

        List<Conta> lista =
                contaRepository.findByPerfil("usuario");

        assertFalse(lista.isEmpty());
        assertEquals("usuario", lista.get(0).getPerfil());

        verify(contaRepository).findByPerfil("usuario");
    }
}
