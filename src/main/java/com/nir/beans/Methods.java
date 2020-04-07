package com.nir.beans;

import com.nir.utils.math.InitialData;
import com.nir.utils.math.Method;
import com.nir.utils.math.MethodInfoJsonPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Methods {

    private static final MethodInfoRepository methodInfoRepository = Beans.methodInfoRepositoryImpl();
    private static final MethodComponent methodComponent = Beans.methodComponent();
    private static final List<Method> methods = new ArrayList<>();
    private static final List<MethodInfoJsonPojo> methodsInfos = new ArrayList<>();

    public static List<String> getNames() {
        initMethodsInfos();
        return methodsInfos.stream().map(MethodInfoJsonPojo::getName).collect(Collectors.toList());
    }

    public static List<Method> getAll() {
        initMethodsInfos();
        initMethods();
        return methods;
    }

    private static void initMethods() {
        if (methods.isEmpty()) {
            final List<Method> created = methodComponent.createOf(methodsInfos);
            methods.addAll(created);
        }
    }

    private static void initMethodsInfos() {
        if (methodsInfos.isEmpty()) {
            final List<MethodInfoJsonPojo> all = methodInfoRepository.getAll();
            methodsInfos.addAll(all);
        }
    }

    public static Method getByName(String name, InitialData initialData) {
        final List<Method> all = getAll();
        final Optional<Method> first = all.stream().filter(it -> it.getName().equals(name)).findFirst();
        final Method method = first.get();
        final int dimension = initialData.getR0().length;
        final double dt = initialData.getDt();
        method.set(dimension, dt);
        return method;
    }
}
