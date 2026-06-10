package br.com.arq.repository;

import br.com.arq.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpf(String cpf);

    @Query("SELECT DISTINCT c FROM Cliente c LEFT JOIN FETCH c.contas LEFT JOIN FETCH c.endereco")
    List<Cliente> findAllComContas();

}