package br.com.arq.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O numero da conta é obrigatorio")
    @Column(name = "numero_conta", unique = true)
    private String numeroConta;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;


	@JsonIgnore
	@OneToMany(mappedBy = "conta", fetch = FetchType.LAZY)
	private List<Transacao> transacoes;


	@NotNull(message = "O saldo inicial deve ser informado")
	@Min(value = 0, message = "O saldo nao pode ser negativo")
	private BigDecimal saldo;

	@NotBlank(message = "O perfil é obrigatorio")
	private String perfil;

	@Column(name = "senha")
	private String senha;

	@Version
	private Long version;

	public void debitar(BigDecimal valor) {
		if (valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("O valor do debito deve ser positivo");
		}
		if (this.saldo.compareTo(valor) < 0) {
			throw new RuntimeException("Saldo insuficiente");
		}
		this.saldo = this.saldo.subtract(valor);
	}

	public void creditar(BigDecimal valor) {
		if (valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("O valor do credito deve ser positivo");
		}
		this.saldo = this.saldo.add(valor);
	}
}