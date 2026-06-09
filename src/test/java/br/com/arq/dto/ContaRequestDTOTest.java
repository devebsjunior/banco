package br.com.arq.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Set;

import br.com.arq.dto.request.ContaRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class ContaRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    @DisplayName("Deve invalidar quando o CPF está em branco")
    void deveInvalidarCpfBranco() {

        ContaRequestDTO dto = new ContaRequestDTO(
                "Edson",
                "", // CPF inválido
                "ed@email.com",
                "Agencia Central",
                "1234",
                "01001000",
                "123456",
                BigDecimal.ZERO,
                "senha123",
                "Rua A",
                "100",
                "Centro",
                "São Paulo",
                "SP"
        );

        Set<ConstraintViolation<ContaRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("CPF é obrigatório")));
    }

}