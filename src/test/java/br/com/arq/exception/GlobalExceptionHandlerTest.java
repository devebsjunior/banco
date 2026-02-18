package br.com.arq.exception;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@WebMvcTest(GlobalExceptionHandler.class)
@Import(GlobalExceptionHandler.class) 
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class TestController {
        @GetMapping("/test-runtime")
        public void throwRuntime() {
            throw new RuntimeException("Saldo insuficiente!");
        }

        @GetMapping("/test-general")
        public void throwGeneral() throws Exception {
            throw new Exception("Erro genérico");
        }
    }

    @Test
    @DisplayName("Deve tratar RuntimeException e retornar erro")
    void deveTratarRuntimeException() throws Exception {
        mockMvc.perform(get("/api/algum-endpoint-que-nao-existe"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(""))); 
    }

    @Test
    @DisplayName("Deve capturar Exception genérica e retornar 500 Internal Server Error")
    void deveTratarExceptionGenerica() throws Exception {
        mockMvc.perform(get("/test-general"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Erro interno no servidor."));
    }
}