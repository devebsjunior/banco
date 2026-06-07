package br.com.arq.asyncecurity.security;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
* Serviço de criptografia de senha utilizando BCrypt.
*/
@Service
public class BcryptService {


    /**
     * Gera um hash da senha.
     */
    public String hash(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }


    /**
     * Valida se a senha corresponde ao hash.
     */
    public boolean matches(String senha, String senhaHash) {
        return BCrypt.checkpw(senha, senhaHash);
    }
}