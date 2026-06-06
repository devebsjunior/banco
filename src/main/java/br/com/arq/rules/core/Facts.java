package br.com.arq.rules.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Facts {

    private final Map<Class<?>, Object> data = new ConcurrentHashMap<>();

    public <T> T get(Class<T> type) {
        return type.cast(data.get(type));
    }

    public <T> Facts add(Class<T> type, T value) {
        data.put(type, value);
        return this;
    }

    public <T> boolean contains(Class<T> type) {
        return data.containsKey(type);
    }
}
