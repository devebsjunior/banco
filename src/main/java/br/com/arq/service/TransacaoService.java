package br.com.arq.service;

import br.com.arq.enums.TipoTransacao;
import br.com.arq.model.Conta;
import br.com.arq.model.Transacao;
import br.com.arq.repository.ContaRepository;
import br.com.arq.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;

    @Transactional
    public void depositar(Long contaId, BigDecimal valor) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        conta.creditar(valor);
        contaRepository.save(conta);

        Transacao transacao = Transacao.builder()
                .conta(conta)
                .numeroConta(conta.getNumeroConta())
                .valor(valor)
                .tipo(TipoTransacao.DEPOSITO) // <<< Ajustado para usar seu Enum real
                .build();

        transacaoRepository.save(transacao);
    }

    @Transactional
    public void sacar(Long contaId, BigDecimal valor) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        conta.debitar(valor);
        contaRepository.save(conta);

        Transacao transacao = Transacao.builder()
                .conta(conta)
                .numeroConta(conta.getNumeroConta())
                .valor(valor.negate())
                .tipo(TipoTransacao.SAQUE) // <<< Ajustado para usar seu Enum real
                .build();

        transacaoRepository.save(transacao);
    }

    @Transactional
    public void transferir(Long contaOrigemId, String numeroContaDestino, BigDecimal valor) {
        Conta origem = contaRepository.findById(contaOrigemId)
                .orElseThrow(() -> new RuntimeException("Sua conta não foi encontrada"));

        Conta destino = contaRepository.findByNumeroConta(numeroContaDestino)
                .orElseThrow(() -> new RuntimeException("Conta de destino não existe"));

        if (origem.getId().equals(destino.getId())) {
            throw new RuntimeException("Não é possível transferir para si mesmo");
        }

        origem.debitar(valor);
        destino.creditar(valor);

        contaRepository.save(origem);
        contaRepository.save(destino);

        Transacao debito = Transacao.builder()
                .conta(origem)
                .numeroConta(origem.getNumeroConta())
                .valor(valor.negate())
                .tipo(TipoTransacao.TRANSFERENCIA) // <<< Verifique no seu Enum se o nome é TRANSFERENCIA ou PIX
                .build();

        Transacao credito = Transacao.builder()
                .conta(destino)
                .numeroConta(destino.getNumeroConta())
                .valor(valor)
                .tipo(TipoTransacao.TRANSFERENCIA) // <<< Use o valor correspondente do seu Enum
                .build();

        transacaoRepository.save(debito);
        transacaoRepository.save(credito);
    }

    @Transactional(readOnly = true)
    public List<Transacao> listarExtrato(Long contaId) {
        return transacaoRepository.findByContaIdOrderByDataHoraDesc(contaId);
    }
}