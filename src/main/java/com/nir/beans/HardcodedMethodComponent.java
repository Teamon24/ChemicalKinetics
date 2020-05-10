package com.nir.beans;

import com.google.common.collect.Lists;
import com.nir.utils.math.method.Method;
import com.nir.utils.math.method.hardcoded.ForwardEuler;
import com.nir.utils.math.method.hardcoded.RungeKutta;

import java.util.List;

public class HardcodedMethodComponent {
    public List<Method> getAll() {

        return Lists.newArrayList(new RungeKutta(4), new RungeKutta(5), ForwardEuler.INSTANCE);
    }
}
