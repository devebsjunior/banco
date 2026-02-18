package br.com.arq.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import br.com.arq.model.Cliente;
import br.com.arq.model.Conta;

@DataJpaTest
@ActiveProfiles("test")  
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ContaRepositoryTest {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setNome("Edson");
        cliente.setCpf("02295351782");
        cliente.setEmail("ed@email.com");
        entityManager.persist(cliente);
    }

    @Test
    @DisplayName("Deve encontrar conta por número com Bloqueio Pessimista")
    void deveEncontrarComLockPessimista() {
        Conta conta = new Conta(null, "123789", cliente, new BigDecimal("1000"), "usuario", "123", 0L);
        entityManager.persist(conta);
        entityManager.flush();

        Optional<Conta> resultado = contaRepository.findByNumeroContaWithLock("123789");

        assertTrue(resultado.isPresent());
        assertEquals("123789", resultado.get().getNumeroConta());
    }

    @Test
    @DisplayName("Deve listar contas pelo perfil corretamente")
    void deveBuscarPorPerfil() {
        Conta c1 = new Conta(null, "111", cliente, BigDecimal.ZERO, "usuario", "123", 0L);
        Conta c2 = new Conta(null, "222", cliente, BigDecimal.ZERO, "admin", "123", 0L);
        entityManager.persist(c1);
        entityManager.persist(c2);

        List<Conta> usuarios = contaRepository.findByPerfil("usuario");

        assertEquals(1, usuarios.size());
        assertEquals("111", usuarios.get(0).getNumeroConta());
    }

    @Test
    @DisplayName("Deve buscar contas através do CPF do cliente vinculado")
    void deveBuscarPorCpfDoCliente() {
        Conta conta = new Conta(null, "999", cliente, BigDecimal.ZERO, "usuario", "123", 0L);
        entityManager.persist(conta);

        List<Conta> resultado = contaRepository.findByClienteCpf("02295351782");

        assertFalse(resultado.isEmpty());
        assertEquals("999", resultado.get(0).getNumeroConta());
    }
}