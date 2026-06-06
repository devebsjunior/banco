package br.com.arq.audit;

import br.com.arq.utils.TimeUtils;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;
    private String perfil;
    private String operacao;
    private boolean sucesso;
    private String mensagem;
    private String regra;
    private String origem;
    private Long tempoExecucaoMs;
    private LocalDateTime dataHora;


    @PrePersist
    public void prePersist() {
        this.dataHora = TimeUtils.now();
    }
}