package br.com.arq.login.handler;


import br.com.arq.login.LoginContext;

public abstract class LoginHandler {

    private LoginHandler next;

    public LoginHandler setNext(LoginHandler next) {
        this.next = next;
        return next;
    }

    public void handle(LoginContext ctx) {

        doHandle(ctx);

        if (next != null) {
            next.handle(ctx);
        }
    }

    protected abstract void doHandle(LoginContext ctx);
}