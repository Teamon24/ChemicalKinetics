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
}
