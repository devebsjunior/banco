package br.com.arq.rules.core.interfaces;

import br.com.arq.rules.core.Facts;

import java.util.function.Predicate;

@FunctionalInterface
public interface When extends Predicate<Facts> {

    @Override
    boolean test(Facts facts);

}