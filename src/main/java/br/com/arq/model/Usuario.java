package br.com.arq.model;

import br.com.arq.utils.TimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;


    @JsonProperty("email")
    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    private String senha;

    @JsonProperty("primeiro_nome")
    private String primeiroNome;

    @JsonProperty("ultimo_nome")
    private String ultimoNome;

    @JsonProperty("token_transacao")
    private String tokenTransacao;

    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    @OneToMany(mappedBy = "usuario",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<Perfil> perfis;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataAlteracao;


    @PrePersist
    public void prePersist() {
        this.dataCriacao = now();
        this.dataAlteracao = now();
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAlteracao = now();
    }

    private LocalDateTime now() {
        return TimeUtils.now();
    }
}
