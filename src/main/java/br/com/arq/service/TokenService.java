package br.com.arq.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import br.com.arq.model.Conta;

@Service
public class TokenService {

    private static final String SECRET_KEY = "omarlilicleo2026@1";
    private static final String ISSUER = "bancodoedsonbelem";

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