package br.com.arq.rules.core.interfaces;


import br.com.arq.rules.core.Facts;

@FunctionalInterface
public interface Then{
    void apply(Facts facts);
}

