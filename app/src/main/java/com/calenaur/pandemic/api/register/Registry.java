package com.calenaur.pandemic.api.register;

import java.util.HashMap;

public class Registry<T> {

    private HashMap<Integer, T> register;

    public Registry() {
        register = new HashMap<>();
    }

    public void clear() {
        register.clear();
    }

    public void register(int id, T value) {
        register.put(id, value);
    }

    public T get(int id) {
        return register.get(id);
    }

    public T[] toArray(T[] a) {
        return register.values().toArray(a);
    }
}
