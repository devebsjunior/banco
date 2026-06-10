package br.com.arq.rules.core;


/**
 * Representa uma chave tipada utilizada para armazenar e recuperar dados dentro do objeto {@link Facts}.
 * Essa classe permite garantir mais segurança de tipo (type-safety), evitando o uso de Strings soltas
 * como chave e reduzindo erros em tempo de execução.</p>
 * *Cada instância de {@code FactKey} possui um nome único que a identifica no mapa interno de dados.</p>
 *
 * @param <T> Tipo do valor associado à chave
 */
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