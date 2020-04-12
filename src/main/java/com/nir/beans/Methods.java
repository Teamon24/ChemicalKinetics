package com.nir.beans;

import com.nir.utils.math.method.Method;
import com.nir.utils.math.method.MethodInfoJsonPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Methods {

    private static final MethodInfoComponent methodInfoComponent = Beans.methodInfoComponent();
    private static final MethodComponent methodComponent = Beans.methodComponent();
    private static final List<Method> methods = new ArrayList<>();
    private static final List<MethodInfoJsonPojo> methodInfoJsonPojos = new ArrayList<>();

    public static List<Method> getAll() {
        initMethodsInfos();
        initMethods();
        return methods;
    }

    public static List<String> getNames() {
        initMethodsInfos();
        return methodInfoJsonPojos
            .stream()
            .map(MethodInfoJsonPojo::getName)
            .collect(Collectors.toList());
    }

    public static Method getByName(String name) {
        final List<Method> all = getAll();
        final Optional<Method> first = all.stream().filter(it -> it.getName().equals(name)).findFirst();
        final Method method = first.get();
        return method;
    }

    private static void initMethodsInfos() {
        if (methodInfoJsonPojos.isEmpty()) {
            final List<MethodInfoJsonPojo> all = methodInfoComponent.getAll();
            methodInfoJsonPojos.addAll(all);
        }
    }

    private static void initMethods() {
        if (methods.isEmpty()) {
            final List<Method> created = methodComponent.create(methodInfoJsonPojos);
            methods.addAll(created);
        }
    }
}
