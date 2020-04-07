package com.nir.utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionUtils {
    public static <T> List<Indexed<T>> withIndex(Collection<T> collection) {
        final Counter counter = new Counter(0);
        return collection.stream().map(t -> new Indexed(counter.getAndIncrement(), t)).collect(Collectors.toList());
    }
}
