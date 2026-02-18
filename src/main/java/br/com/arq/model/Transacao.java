package br.com.arq.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroConta;
    private String tipo; 
    private BigDecimal valor;
    private LocalDateTime dataHora;

    @PrePersist
    protected void onCreate() {
        dataHora = LocalDateTime.now();
    }
}