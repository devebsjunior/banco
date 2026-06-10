package br.com.arq.repository;

import br.com.arq.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("SELECT t FROM Transacao t WHERE t.conta.id = :contaId ORDER BY t.dataHora DESC")
    List<Transacao> findByContaIdOrderByDataHoraDesc(@Param("contaId") Long contaId);

    List<Transacao> findByNumeroContaOrderByDataHoraDesc(String numeroConta);
}