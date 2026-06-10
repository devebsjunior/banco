package br.com.arq.rules.core;



import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Container de dados (fatos) utilizado pelo motor de regras.
 *  Armazena informações necessárias para avaliação e execução das regras.</p>
 * É thread-safe, utilizando {@link java.util.concurrent.ConcurrentHashMap}.</p>
 *
 *   Suporta três formas de armazenamento:
 *     Chaves tipadas ({@link FactKey}) - recomendado
 *     _ Classe (Class)
 *     _ String
 * </ul>
 * </p>
 */
public class Facts {


        private final Map<Object, Object> data =
                new ConcurrentHashMap<>();

        /*
         * NOVO MODELO
         */
        public <T> Facts add(
                FactKey<T> key,
                T value
        ) {

            data.put(key, value);
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T> T get(
                FactKey<T> key
        ) {

            return (T) data.get(key);
        }

        /*
         * MODELO ANTIGO
         */
        public <T> Facts add(
                Class<T> type,
                T value
        ) {

            data.put(type, value);
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T> T get(
                Class<T> type
        ) {

            return (T) data.get(type);
        }

        /*
         * STRING (caso queira usar FactNames)
         */
        public Facts add(
                String key,
                Object value
        ) {

            data.put(key, value);
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T> T get(
                String key
        ) {

            return (T) data.get(key);
        }

        public boolean contains(
                Object key
        ) {

            return data.containsKey(key);
        }

        public void remove(
                Object key
        ) {

            data.remove(key);
        }
    }