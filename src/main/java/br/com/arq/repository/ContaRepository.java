package br.com.arq.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arq.model.Conta;
import jakarta.persistence.LockModeType;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByPerfil(String perfil);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Conta c WHERE c.numeroConta = :numero")
    Optional<Conta> findByNumeroContaWithLock(@Param("numero") String numero);

    Optional<Conta> findByNumeroConta(String numero);
    
    @Query("SELECT c FROM Conta c WHERE c.cliente.cpf = :cpf")
    List<Conta> findByClienteCpf(@Param("cpf") String cpf);
}