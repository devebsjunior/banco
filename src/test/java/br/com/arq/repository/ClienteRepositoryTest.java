package br.com.arq.repository;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.arq.model.Cliente;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClienteRepositoryTest {



        @Autowired
        private ClienteRepository clienteRepository;

        @Test
        @DisplayName("Deve salvar cliente")
        void deveSalvarCliente() {

            Cliente cliente = new Cliente();

            cliente.setNome("Edson");
            cliente.setCpf("02295351782");
            cliente.setEmail("ed@email.com");

            Cliente salvo =
                    clienteRepository.save(cliente);

            assertNotNull(salvo.getId());

            System.out.println(
                    "Cliente salvo ID = " +
                            salvo.getId()
            );
        }

        @Test
        @DisplayName("Deve buscar cliente por CPF")
        void deveBuscarClientePorCpf() {

            Cliente cliente = new Cliente();

            cliente.setNome("Edson");
            cliente.setCpf("02295351782");
            cliente.setEmail("ed@email.com");

            clienteRepository.save(cliente);

            Optional<Cliente> encontrado =
                    clienteRepository.findByCpf(
                            "02295351782"
                    );

            assertTrue(encontrado.isPresent());

            assertEquals(
                    "Edson",
                    encontrado.get().getNome()
            );

            System.out.println(
                    "Cliente encontrado = " +
                            encontrado.get().getNome()
            );
        }

        @Test
        @DisplayName("Não deve encontrar CPF inexistente")
        void naoDeveEncontrarCpfInexistente() {

            Optional<Cliente> encontrado =
                    clienteRepository.findByCpf(
                            "99999999999"
                    );

            assertFalse(
                    encontrado.isPresent()
            );
        }

        @Test
        @DisplayName("Deve atualizar cliente")
        void deveAtualizarCliente() {

            Cliente cliente = new Cliente();

            cliente.setNome("Edson");
            cliente.setCpf("02295351782");
            cliente.setEmail("ed@email.com");

            Cliente salvo =
                    clienteRepository.save(cliente);

            salvo.setNome(
                    "Edson Belém"
            );

            clienteRepository.save(salvo);

            Cliente atualizado =
                    clienteRepository
                            .findById(
                                    salvo.getId()
                            )
                            .orElseThrow();

            assertEquals(
                    "Edson Belém",
                    atualizado.getNome()
            );
        }

        @Test
        @DisplayName("Deve remover cliente")
        void deveRemoverCliente() {

            Cliente cliente = new Cliente();

            cliente.setNome("Edson");
            cliente.setCpf("02295351782");
            cliente.setEmail("ed@email.com");

            Cliente salvo =
                    clienteRepository.save(cliente);

            clienteRepository.deleteById(
                    salvo.getId()
            );

            assertFalse(
                    clienteRepository
                            .findById(
                                    salvo.getId()
                            )
                            .isPresent()
            );
        }

        @Test
        @DisplayName("Deve contar clientes")
        void deveContarClientes() {

            Cliente c1 = new Cliente();
            c1.setNome("Cliente 1");
            c1.setCpf("11111111111");
            c1.setEmail("c1@email.com");

            Cliente c2 = new Cliente();
            c2.setNome("Cliente 2");
            c2.setCpf("22222222222");
            c2.setEmail("c2@email.com");

            clienteRepository.save(c1);
            clienteRepository.save(c2);

            long total =
                    clienteRepository.count();

            assertEquals(
                    2,
                    total
            );

            System.out.println(
                    "Total clientes = " +
                            total
            );
        }
    }