package br.com.arq.rules.core;


public final class FactKey<T> {

    private final String name;

    public FactKey(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}