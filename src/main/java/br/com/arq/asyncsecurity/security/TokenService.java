package br.com.arq.asyncsecurity.security;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import br.com.arq.model.Conta;
/**
 * Serviço responsável pelo gerenciamento de sessões do usuário.
 * Controla criação, validação, remoção e invalidação de sessões.
 * @author Edson Belém
 * @version 1.0
 * @since 2026
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Algorithm algorithm;

    @jakarta.annotation.PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secretKey);
        logger.info("TokenService inicializado com sucesso");
    }


    /**
     * Gera um token JWT com dados da conta.
     * @param conta objeto da conta que será autenticada
     * @return token JWT gerado
     * @throws RuntimeException caso ocorra erro na criação do token
     */
    public String gerarToken(Conta conta) {
        try {
            String token = JWT.create()
                    .withIssuer(issuer)
                    .withSubject(conta.getNumeroConta())
                    .withClaim("nome", conta.getCliente().getNome())
                    .withClaim("perfil", conta.getPerfil())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                    .sign(algorithm);

            logger.debug("Token gerado com sucesso para conta: {}", conta.getNumeroConta());
            return token;
        } catch (JWTCreationException exception) {
            logger.error("Erro ao gerar token JWT para conta: {}",
                    conta.getNumeroConta(), exception);
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
            String subject = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();

            logger.debug("Token validado com sucesso para conta: {}", subject);
            return subject;
        } catch (JWTVerificationException exception) {
            logger.warn("Falha na validação do token JWT", exception);
            return "";
        } catch (Exception e) {
            logger.error("Erro inesperado ao validar token JWT", e);
            return "";
        }
    }
}