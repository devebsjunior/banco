package br.com.arq.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.arq.dto.TransferenciaDTO;
import br.com.arq.repository.ContaRepository;
import br.com.arq.service.ContaService;

@WebMvcTest(UsuarioController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContaService contaService;

    @MockBean
    private ContaRepository repository; 

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve retornar 200 ao realizar depósito com sucesso")
    void deveDepositarComSucesso() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("valor", new BigDecimal("100.00"));

        mockMvc.perform(post("/api/usuarios/contas/123789/deposito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Depósito de R$ 100.00 realizado com sucesso!"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao sacar valor maior que o saldo")
    void deveRetornarErroSaqueInvalido() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("valor", new BigDecimal("5000.00"));

        doThrow(new RuntimeException("Saldo insuficiente para realizar o saque."))
            .when(contaService).sacar(anyString(), any(BigDecimal.class));

        mockMvc.perform(post("/api/usuarios/contas/123789/saque")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Saldo insuficiente para realizar o saque."));
    }

    @Test
    @DisplayName("Deve realizar transferência com sucesso")
    void deveTransferirComSucesso() throws Exception {
        TransferenciaDTO dto = new TransferenciaDTO("111", "222", new BigDecimal("100.00"));

        mockMvc.perform(post("/api/usuarios/contas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transferência realizada com sucesso"));
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar extrato vazio")
    void deveVerExtrato() throws Exception {
        when(contaService.buscarExtrato("123789")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/usuarios/contas/123789/extrato"))
                .andExpect(status().isOk());
    }
}