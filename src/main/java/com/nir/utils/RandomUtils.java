package com.nir.utils;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

/**
 *
 */
public class RandomUtils {

    private static final Random random = new Random();

    public static <T> T randomIn(Collection<T> collection) {
        final int size = collection.size();
        final int index = random.nextInt(size);
        final Optional<Indexed<T>> first = CollectionUtils.withIndex(collection).stream().filter(tIndexed -> tIndexed.getIndex() == index).findFirst();
        return first.get().getValue();
    }
}
