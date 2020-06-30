package com.calenaur.pandemic.api.register;

public class KeyPair {

    private final int k1;
    private final int k2;

    public KeyPair(int k1, int k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyPair)) return false;
        KeyPair key = (KeyPair) o;
        return k1 == key.k1 && k2 == key.k2;
    }

    @Override
    public int hashCode() {
        int result = k1;
        result = 31 * result + k2;
        return result;
    }

}
