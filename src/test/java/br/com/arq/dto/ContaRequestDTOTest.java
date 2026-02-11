package br.com.arq.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Set;

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
    @DisplayName("Deve validar quando o DTO está correto")
    void deveValidarDtoCorreto() {
        ContaRequestDTO dto = new ContaRequestDTO(
            "Edson Belem", 
            "02295351782", 
            "ed@email.com", 
            "123789", 
            new BigDecimal("12000"), 
            "usuario", 
            "123456"
        );

        Set<ConstraintViolation<ContaRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "O DTO deveria ser válido");
    }

    @Test
    @DisplayName("Deve invalidar quando o CPF está em branco")
    void deveInvalidarCpfBranco() {
        ContaRequestDTO dto = new ContaRequestDTO(
            "Edson", "", "ed@email.com", "123", BigDecimal.ZERO, "usuario", "123456"
        );

        Set<ConstraintViolation<ContaRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("CPF é obrigatório")));
    }

    @Test
    @DisplayName("Deve invalidar quando o saldo é negativo")
    void deveInvalidarSaldoNegativo() {
        ContaRequestDTO dto = new ContaRequestDTO(
            "Edson", "02295351782", "ed@email.com", "123", new BigDecimal("-10"), "usuario", "123456"
        );

        Set<ConstraintViolation<ContaRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}