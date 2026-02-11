package br.com.arq.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

class ClienteTest {

    @Test
    @DisplayName("Deve garantir que o Lombok está gerando getters, setters e construtores corretamente")
    void deveTestarGettersESetters() {
        Cliente cliente = new Cliente();
        cliente.setNome("Edson Belem");
        cliente.setCpf("02295351782");
        cliente.setEmail("ed@email.com");
        cliente.setContas(new ArrayList<>());

        assertEquals("Edson Belem", cliente.getNome());
        assertEquals("02295351782", cliente.getCpf());
        assertEquals("ed@email.com", cliente.getEmail());
        assertNotNull(cliente.getContas());
    }

    @Test
    @DisplayName("Deve testar o construtor com todos os argumentos (AllArgsConstructor)")
    void deveTestarConstrutorCompleto() {
        Cliente cliente = new Cliente(1L, "Edson", "12345678901", "edson@email.com", null);
        
        assertAll("cliente",
            () -> assertEquals(1L, cliente.getId()),
            () -> assertEquals("Edson", cliente.getNome()),
            () -> assertEquals("12345678901", cliente.getCpf())
        );
    }
}