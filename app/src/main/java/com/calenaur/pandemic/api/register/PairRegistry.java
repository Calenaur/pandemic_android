package com.calenaur.pandemic.api.register;

import java.util.HashMap;

public class PairRegistry<T> {
    private HashMap<KeyPair, T> register;

    public PairRegistry() {
        register = new HashMap<>();
    }

    public void clear() {
        register.clear();
    }

    public void register(KeyPair keyPair, T value) {
        register.put(keyPair, value);
    }

    public T get(KeyPair keyPair) {
        return register.get(keyPair);
    }

    public boolean containsKey(KeyPair keyPair) {
        return register.containsKey(keyPair);
    }

    public T[] toArray(T[] a) {
        return register.values().toArray(a);
    }
}
