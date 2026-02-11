package br.com.arq.repository;

import br.com.arq.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByNumeroContaOrderByDataHoraDesc(String numeroConta);
}