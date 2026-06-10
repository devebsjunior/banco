package br.com.arq.asyncsecurity;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.arq.model.Conta;
import br.com.arq.repository.ContaRepository;
import br.com.arq.asyncsecurity.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	private final ContaRepository contaRepository;
	private final TokenService tokenService;

	@Transactional(readOnly = true)
	public Map<String, Object> autenticar(String login, String senha) {
		try {
			logger.debug("Iniciando autenticação para usuário: {}", login);

			if (login == null || login.trim().isEmpty()) {
				logger.warn("Tentativa de login com usuário vazio");
				throw new RuntimeException("Login não informado");
			}
			if (senha == null || senha.trim().isEmpty()) {
				logger.warn("Tentativa de login com senha vazia para usuário: {}", login);
				throw new RuntimeException("Senha não informada");
			}
			Conta conta = contaRepository.findByClienteEmail(login)
					.orElseThrow(() -> {
						logger.warn("Conta não encontrada para o e-mail: {}", login);
						return new RuntimeException("Usuário ou senha inválidos");
					});
			if (!org.mindrot.jbcrypt.BCrypt.checkpw(senha.trim(), conta.getSenha().trim())) {
				logger.warn("Falha na autenticação - senha incorreta para login: {}", login);
				throw new RuntimeException("Senha incorreta!");
			}
			logger.info("Autenticação bem-sucedida para login: {}", login);
			String token = tokenService.gerarToken(conta);
			Map<String, Object> response = Map.of(
				"token", token,
				"nome", conta.getCliente().getNome(),
				"perfil", conta.getPerfil(),
				"numeroConta", conta.getNumeroConta(),
				"saldo", conta.getSaldo()
			);
			logger.debug("Token gerado e resposta preparada para: {}", login);
			return response;
		} catch (RuntimeException e) {
			logger.error("Erro na autenticação para login: {}", login, e);
			throw e;
		} catch (Exception e) {
			logger.error("Erro inesperado na autenticação para login: {}", login, e);
			throw new RuntimeException("Erro ao autenticar", e);
		}
	}

}
