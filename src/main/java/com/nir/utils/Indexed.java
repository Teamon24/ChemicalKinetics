package com.nir.utils;

public class Indexed<T> implements Comparable<Indexed<T>> {
    private Integer index;
    private T t;

    public Indexed(Integer index, T t) {
        this.index = index;
        this.t = t;
    }

    @Override
    public int compareTo(Indexed<T> tIndexed) {
        return this.index.compareTo(tIndexed.index);
    }

    public int getIndex() {
        return this.index;
    }

    public T getValue() {
        return this.t;
    }
}
