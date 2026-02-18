package br.com.arq.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

class TransacaoTest {

    @Test
    @DisplayName("Deve garantir que o Builder do Lombok cria o objeto corretamente")
    void deveTestarBuilderETipos() {
        Transacao transacao = Transacao.builder()
                .numeroConta("123789")
                .tipo("CREDITO")
                .valor(new BigDecimal("500.00"))
                .build();

        assertAll("transacao",
            () -> assertEquals("123789", transacao.getNumeroConta()),
            () -> assertEquals("CREDITO", transacao.getTipo()),
            () -> assertEquals(new BigDecimal("500.00"), transacao.getValor())
        );
    }

    @Test
    @DisplayName("Deve garantir que o método onCreate (PrePersist) atribui uma data")
    void deveTestarPrePersist() {
        Transacao transacao = new Transacao();
        assertNull(transacao.getDataHora(), "Data deve ser nula antes do onCreate");
        transacao.onCreate();
        assertNotNull(transacao.getDataHora(), "Data não deve ser nula após o onCreate");
        assertTrue(transacao.getDataHora().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Deve testar construtor completo e Getters/Setters")
    void deveTestarGettersESetters() {
        LocalDateTime agora = LocalDateTime.now();
        Transacao transacao = new Transacao(1L, "123", "DEBITO", BigDecimal.TEN, agora);

        assertEquals(1L, transacao.getId());
        assertEquals(agora, transacao.getDataHora());
    }
}