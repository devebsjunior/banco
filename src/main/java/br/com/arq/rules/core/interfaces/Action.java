package br.com.arq.rules.core.interfaces;


import br.com.arq.rules.core.Facts;

@FunctionalInterface
public interface Action {
    void execute(Facts facts);
}
