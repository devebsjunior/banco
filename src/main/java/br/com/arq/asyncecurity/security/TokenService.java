package br.com.arq.asyncecurity.security;

import java.util.Date;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import br.com.arq.model.Conta;


/**
 * Serviço responsável pelo gerenciamento de sessões do usuário.
 * Controla criação, validação, remoção e invalidação de sessões.
 * @author Edson Belém
 * @version 1.0
 * @since 2026
 */
@Service
public class TokenService {
    private static final String SECRET_KEY = "omarlilicleo2026@1";
    private static final String ISSUER = "bancoBrasilInvestidores";


    /**
     * Gera um token JWT com dados da conta.
     * @param conta objeto da conta que será autenticada
     * @return token JWT gerado
     * @throws RuntimeException caso ocorra erro na criação do token
     */
    public String gerarToken(Conta conta) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(conta.getNumeroConta())
                    .withClaim("nome", conta.getCliente().getNome())
                    .withClaim("perfil", conta.getPerfil())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    /**
     * Valida o token JWT recebido.
     *
     * @param token token JWT enviado pelo cliente
     * @return número da conta se válido, vazio se inválido
     */
    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e) {
            return "";
        }
    }
}