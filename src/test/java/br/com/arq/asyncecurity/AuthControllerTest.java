//package br.com.arq.auth;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import br.com.arq.dto.LoginRequestDTO;
//import br.com.arq.model.Conta;
//
//@WebMvcTest(AuthController.class)
//@ActiveProfiles("test")
//@AutoConfigureMockMvc(addFilters = false)
//public class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AuthService authService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("Deve retornar 200 e os dados do usuário ao logar com sucesso")
//    void deveLogarComSucesso() throws Exception {
//        LoginRequestDTO dto = new LoginRequestDTO("123789", "123456");
//        
//        Conta contaMock = new Conta();
//        contaMock.setNumeroConta("123789");
//        contaMock.setPerfil("usuario");
//
//        when(authService.autenticar(anyString(), anyString())).thenReturn(contaMock);
//
//        mockMvc.perform(post("/api/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.numeroConta").value("123789"));
//    }
//
//    @Test
//    @DisplayName("Deve retornar 401 quando a senha for inválida")
//    void deveRetornar401QuandoSenhaErrada() throws Exception {
//        LoginRequestDTO dto = new LoginRequestDTO("ed@email.com", "senha_errada");
//
//        when(authService.autenticar(anyString(), anyString()))
//            .thenThrow(new RuntimeException("Credenciais inválidas"));
//
//        mockMvc.perform(post("/api/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isUnauthorized());
//    }
//}