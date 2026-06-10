package br.com.arq.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {

    @Test
    void deveCriarEndereco() {

        Cliente cliente = new Cliente();
        cliente.setNome("Edson");

        Endereco endereco = Endereco.builder()
                .id(1L)
                .logradouro("Rua A")
                .numero("100")
                .bairro("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .cep("01001000")
                .cliente(cliente)
                .build();

        assertNotNull(endereco);
        assertEquals("Rua A", endereco.getLogradouro());
        assertEquals("São Paulo", endereco.getCidade());
        assertNotNull(endereco.getCliente());
    }
}
