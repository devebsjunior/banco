package br.com.arq.audit;

import br.com.arq.utils.TimeUtils;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Representa um log técnico da aplicação.
 * Usado para registrar eventos de execução,
 * erros, avisos e mensagens informativas.
 */
@Entity
@Table(name = "app_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nivel;

    private String mensagem;

    private String origem;

    private String error;


    @Column(columnDefinition = "TEXT")
    private String stackTrace;


    private LocalDateTime dataHora;

    /**
     * Define  a data/hora com fuso da aplicação
     */
    @PrePersist
    public void prePersist() {
        this.dataHora = TimeUtils.now();
    }
}