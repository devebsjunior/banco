package br.com.arq.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import br.com.arq.asyncsecurity.interceptor.AccessControlInterceptor;
import br.com.arq.model.Conta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.arq.dto.request.ContaRequestDTO;
import br.com.arq.service.ContaService;


@ActiveProfiles("test")
@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

	@MockBean
	private AccessControlInterceptor interceptor;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ContaService contaService;

	@Autowired
	private ObjectMapper objectMapper;

	private ContaRequestDTO criar() {
		return new ContaRequestDTO(
				"Edson",
				"12345678901",
				"edson@email.com",
				"Agencia Central",
				"0001",
				"01001000",
				"001",
				BigDecimal.valueOf(2000),
				"123456",
				"Rua A",
				"100",
				"Centro",
				"São Paulo",
				"SP"
		);
	}

	@Test
	@DisplayName("Deve criar conta com sucesso")
	void deveCriarContaComSucesso() throws Exception {

		ContaRequestDTO dto = criar();

		Conta conta = new Conta();
		conta.setId(1L);
		conta.setNumeroConta("123456");

		when(contaService.criarConta(any()))
				.thenReturn(conta);

		mockMvc.perform(post("/api/admin/contas")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk());
	}

}