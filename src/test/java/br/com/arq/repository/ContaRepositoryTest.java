package br.com.arq.repository;



import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import br.com.arq.dto.DadosAgencia;
import br.com.arq.dto.DadosCliente;
import br.com.arq.dto.DadosConta;
import br.com.arq.model.Agencia;
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
    private Agencia agencia;

//    @BeforeEach
//    void setUp() {
//
//        cliente = new Cliente();
//        cliente.setNome("Teste");
//        cliente.setCpf("12345678900");
//        cliente.setEmail("teste@email.com");
//
//        entityManager.persist(cliente);
//
//        agencia = new Agencia();
//        agencia.setNomeAgencia("Banco Teste");
//        agencia.setNumeroAgencia("0001");
//        agencia.setCodigo("001");
//
//        entityManager.persist(agencia);
//
//        Conta conta = new Conta();
//        conta.setNumeroConta("111111");
//        conta.setSaldo(BigDecimal.ZERO);
//        conta.setCliente(cliente);
//        conta.setPerfil("usuario");
//        conta.setAgencia(agencia);
//
//        entityManager.persist(conta);
//        entityManager.flush();
//    }

//    @Test
//    @DisplayName("Deve encontrar conta por número com Bloqueio Pessimista")
//    void deveEncontrarComLockPessimista() {
//        DadosCliente dadosCliente = new DadosCliente(
//                "Edson",
//                "02295351782",
//                "edson@email.com"
//        );
//
//        DadosAgencia dadosAgencia = new DadosAgencia(
//                "Banco Teste",
//                "0001",
//                "001"
//        );
//
//        DadosConta dadosConta = new DadosConta(
//                UUID.randomUUID().toString(), // ✅ evita conflito
//                "usuario",
//                "123",
//                BigDecimal.ZERO
//        );
//
//
//        Conta conta = Conta.criarContaCompleta(
//                dadosCliente,
//                dadosAgencia,
//                dadosConta
//        );
//
//
//        entityManager.persist(conta.getCliente());
//        entityManager.persist(conta.getAgencia());
//        entityManager.persist(conta);
//
//        entityManager.flush();
//
//        Optional<Conta> resultado =
//                contaRepository.findByNumeroContaWithLock(dadosConta.numeroConta());
//
//        assertTrue(resultado.isPresent());
//        assertEquals(dadosConta.numeroConta(), resultado.get().getNumeroConta());
//
//    }
}