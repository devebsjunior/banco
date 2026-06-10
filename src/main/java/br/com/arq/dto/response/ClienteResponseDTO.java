package br.com.arq.dto.response;


import br.com.arq.model.Cliente;
import java.util.List;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        List<ContaResponseDTO> contas
) {
    public ClienteResponseDTO(Cliente cliente) {
        this(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getContas() != null ?
                        cliente.getContas().stream().map(ContaResponseDTO::new).toList() : List.of()
        );
    }
}