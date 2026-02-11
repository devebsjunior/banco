package br.com.arq.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.arq.model.Conta;
import br.com.arq.repository.ClienteRepository;
import br.com.arq.repository.ContaRepository;
import br.com.arq.repository.TransacaoRepository;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private ContaService contaService;

    @Test
    @DisplayName("Não deve permitir saque se o saldo for insuficiente")
    void testeSaqueSaldoInsuficiente() {
        Conta conta = new Conta();
        conta.setNumeroConta("123789");
        conta.setSaldo(new BigDecimal("100.00"));

        when(contaRepository.findByNumeroContaWithLock("123789"))
            .thenReturn(Optional.of(conta));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            contaService.sacar("123789", new BigDecimal("150.00"));
        });
        assertEquals("Saldo insuficiente para realizar o saque.", exception.getMessage());
        verify(contaRepository, never()).save(any()); 
    }

    @Test
    @DisplayName("Deve realizar depósito com sucesso")
    void testeDepositoSucesso() {
        Conta conta = new Conta();
        conta.setNumeroConta("123789");
        conta.setSaldo(new BigDecimal("500.00"));

        when(contaRepository.findByNumeroContaWithLock("123789"))
            .thenReturn(Optional.of(conta));

        contaService.depositar("123789", new BigDecimal("200.00"));

        assertEquals(new BigDecimal("700.00"), conta.getSaldo());
        verify(contaRepository, times(1)).save(conta);  
    }
}