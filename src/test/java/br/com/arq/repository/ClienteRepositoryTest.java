package br.com.arq.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.arq.model.Cliente;

@DataJpaTest  
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Deve encontrar um cliente pelo CPF com sucesso")
    void deveEncontrarPorCpf() {
        Cliente cliente = new Cliente(null, "Edson", "02295351782", "ed@email.com", null);
        clienteRepository.save(cliente);
        Optional<Cliente> encontrado = clienteRepository.findByCpf("02295351782");
        assertTrue(encontrado.isPresent());
        assertEquals("Edson", encontrado.get().getNome());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar um CPF que não existe")
    void naoDeveEncontrarCpfInexistente() {
        Optional<Cliente> encontrado = clienteRepository.findByCpf("99999999999");
        assertTrue(encontrado.isEmpty());
    }

    @Test
    @DisplayName("Deve encontrar um cliente pelo Email com sucesso")
    void deveEncontrarPorEmail() {
        Cliente cliente = new Cliente(null, "Edson", "12345678901", "edson@email.com", null);
        clienteRepository.save(cliente);
        Optional<Cliente> encontrado = clienteRepository.findByEmail("edson@email.com");
        assertTrue(encontrado.isPresent());
        assertEquals("12345678901", encontrado.get().getCpf());
    }
}