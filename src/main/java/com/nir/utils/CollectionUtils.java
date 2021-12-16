package com.nir.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {
    public static <T> List<Indexed<T>> withIndex(Collection<T> collection) {
        final Counter counter = new Counter(0);
        final List<Indexed<T>> indexeds = new ArrayList<>();
        collection.forEach(t -> indexeds.add(new Indexed<>(counter.getAndIncrement(), t)));
        return indexeds;
    }

    public static class Indexed<T> implements Comparable<Indexed<T>> {
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
}
