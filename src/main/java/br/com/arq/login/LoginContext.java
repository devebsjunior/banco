package br.com.arq.login;

 import br.com.arq.model.Usuario;
 import lombok.Getter;
 import lombok.Setter;

@Getter
@Setter
public class LoginContext {

    private String email;
    private String senha;

    private Usuario usuario;
    private boolean autorizado;
    private String token;

    public LoginContext(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }




}
