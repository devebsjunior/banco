package br.com.arq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "agencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    private String codigo;
    @NotNull(message = "O nome da agência é obrigatório")
    private String nomeAgencia;
    @NotNull(message = "O número da agência é obrigatório")
    private String numeroAgencia;

     @JsonIgnore
    private String logradouro;
    @JsonIgnore
    private String numero;
    @JsonIgnore
    private String bairro;
    @JsonIgnore
    private String cidade;
    @JsonIgnore
    private String estado;
    @JsonIgnore
    private String cep;

    @OneToMany(
            mappedBy = "agencia",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Conta> contas;

}

