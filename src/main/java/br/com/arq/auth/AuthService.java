package br.com.arq.auth;

import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import br.com.arq.model.Conta;
import br.com.arq.repository.ContaRepository;
import br.com.arq.service.TokenService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final ContaRepository contaRepository;
	private final TokenService tokenService;

	public Map<String, Object> autenticar(String login, String senha) {
		String hashTeste = org.mindrot.jbcrypt.BCrypt.hashpw("123456", org.mindrot.jbcrypt.BCrypt.gensalt());
		boolean funciona = org.mindrot.jbcrypt.BCrypt.checkpw("123456", hashTeste);
		System.out.println(">>> O BCrypt consegue validar o que ele mesmo cria? " + funciona);

		Conta conta = contaRepository.findByNumeroConta(login)
				.orElseThrow(() -> new RuntimeException("Conta não encontrada"));

		if (!org.mindrot.jbcrypt.BCrypt.checkpw(senha.trim(), conta.getSenha().trim())) {
			System.out.println(">>> FALHOU: Senha digitada: [" + senha + "] Hash no banco: [" + conta.getSenha() + "]");
			throw new RuntimeException("Senha incorreta!");
		}

		String token = tokenService.gerarToken(conta);
		return Map.of("token", token, "nome", conta.getCliente().getNome(), "perfil", conta.getPerfil(), "numeroConta",
				conta.getNumeroConta(), 
				"saldo", conta.getSaldo()  
		);
	}

}
