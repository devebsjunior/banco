package br.com.arq.repository;

import br.com.arq.model.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgenciaRepository  extends JpaRepository<Agencia, Long> {

    Optional<Agencia> findByNumeroAgencia(
            String numeroAgencia
    );
}