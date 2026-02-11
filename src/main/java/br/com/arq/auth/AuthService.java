package br.com.arq.auth;

import org.springframework.stereotype.Service;

import br.com.arq.model.Conta;
import br.com.arq.repository.ContaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ContaRepository contaRepository;

    public Conta autenticar(String login, String senha) {
        Conta conta = contaRepository.findByNumeroConta(login)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada: " + login));

        if (!conta.getSenha().equals(senha)) {
            throw new RuntimeException("Senha incorreta!");
        }

        return conta;
    }
}
