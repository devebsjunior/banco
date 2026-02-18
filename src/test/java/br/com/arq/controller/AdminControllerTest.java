//package br.com.arq.controller;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.math.BigDecimal;
//import java.util.Collections;
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
//import br.com.arq.dto.ContaRequestDTO;
//import br.com.arq.service.ContaService;
//
//@WebMvcTest(AdminController.class)
//@ActiveProfiles("test")
//@AutoConfigureMockMvc(addFilters = false)
//public class AdminControllerTest {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@MockBean
//	private ContaService contaService;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@Test
//	void deveTratarRuntimeException() throws Exception {
//	    mockMvc.perform(get("/api/admin/contas/erro-proposital"))
//	            .andExpect(status().isInternalServerError());
//	}
//
//	@Test
//	@DisplayName("Deve retornar 400 Bad Request se o Service lançar exceção")
//	void deveRetornarErroQuandoServiceFalha() throws Exception {
//		ContaRequestDTO dto = new ContaRequestDTO("123", "Nome", "email@test.com", "123", BigDecimal.ZERO, "admin",
//				"123");
//
//		when(contaService.criarConta(any())).thenThrow(new RuntimeException("CPF já cadastrado"));
//
//		mockMvc.perform(post("/api/admin/contas").contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());
//	}
//
//	@Test
//	@DisplayName("Deve retornar 200 OK ao listar as contas")
//	void deveListarContasComSucesso() throws Exception {
//		when(contaService.listarTodas()).thenReturn(Collections.emptyList());
//
//		mockMvc.perform(get("/api/admin/contas")).andExpect(status().isOk());
//	}
//}