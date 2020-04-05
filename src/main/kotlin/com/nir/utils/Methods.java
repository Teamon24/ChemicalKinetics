package com.nir.utils;

import com.google.common.collect.Lists;
import com.nir.utils.math.Euler;
import com.nir.utils.math.InitialData;
import com.nir.utils.math.Method;
import com.nir.utils.math.RungeKutta;

import java.util.List;
import java.util.Optional;

public class Methods {
    public static List<Method> getAll() {
        return Lists.newArrayList(
            Euler.INSTANCE,
            new RungeKutta(4)
        );
    }

    public static Optional<Method> getByName(String name) {
        final List<Method> all = getAll();
        return all.stream().filter(it -> it.getName().equals(name)).findFirst();
    }
}
