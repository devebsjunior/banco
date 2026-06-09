package br.com.arq.exception;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.arq.asyncsecurity.interceptor.AccessControlInterceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;


@WebMvcTest(controllers = GlobalExceptionHandlerTest.TestController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private AccessControlInterceptor interceptor;


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

//    @Test
//    @DisplayName("Deve tratar RuntimeException e retornar 500")
//    void deveTratarRuntimeException() throws Exception {
//
//        mockMvc.perform(get("/test-runtime"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().string(containsString("Erro interno no servidor")));
//    }

}