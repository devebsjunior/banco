package br.com.arq.model;

import br.com.arq.enums.TipoPerfil;
import br.com.arq.utils.TimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(length = 250)
    @JsonIgnore
    private String tokenTransacao;


    @OneToMany(
            mappedBy = "usuario",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Perfil> perfis;

    @JsonProperty("data_criacao")
    private LocalDateTime dataCriacao;

    @JsonProperty("data_alteracao")
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

    public Usuario(String username, String email, String senha, List<Perfil> perfis) {
        this.username = username;
        this.email = email;
        this.senha = senha;
        this.perfis = perfis;
    }
}
