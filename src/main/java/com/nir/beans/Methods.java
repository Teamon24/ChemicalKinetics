package com.nir.beans;

import com.nir.utils.math.method.Method;
import com.nir.utils.math.method.generalized.GeneralizedMethodInfoJsonPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Methods {

    private static final GeneralizedMethodInfoComponent generalizedMethodInfoComponent = Beans.generalizedMethodInfoComponent();
    private static final GeneralizedMethodComponent generalizedMethodComponent = Beans.generalizedMethodComponent();
    private static final HardcodedMethodComponent hardcodedMethodComponent = Beans.hardcodedMethodComponent();
    private static final List<Method> generalizedMethods = new ArrayList<>();
    private static final List<Method> hardcodedMethods = new ArrayList<>();
    private static final List<Method> allMethods = new ArrayList<>();
    private static final List<GeneralizedMethodInfoJsonPojo> generalizedMethodInfoJsonPojos = new ArrayList<>();

    public static List<Method> getAll() {
        initGeneralizedMethods();
        initHardcodedMethods();
        initAllMethods();
        return allMethods;
    }

    public static List<String> getNames() {
        return getAll().stream().map(Method::getName).collect(Collectors.toList());
    }

    private static void initAllMethods() {
        if(allMethods.isEmpty()) {
            allMethods.addAll(hardcodedMethods);
            allMethods.addAll(generalizedMethods);
        }
    }

    public static Method getByName(String name) {
        final List<Method> all = getAll();
        final Optional<Method> first = all.stream().filter(it -> it.getName().equals(name)).findFirst();
        final Method method = first.get();
        return method;
    }

    private static void initHardcodedMethods() {
        if (hardcodedMethods.isEmpty()) {
            final List<Method> created = hardcodedMethodComponent.getAll();
            hardcodedMethods.addAll(created);
        }
    }

    private static void initGeneralizedMethods() {
        initGeneralizedMethodsInfos();
        if (generalizedMethods.isEmpty()) {
            final List<Method> created = generalizedMethodComponent.create(generalizedMethodInfoJsonPojos);
            generalizedMethods.addAll(created);
        }
    }

    private static void initGeneralizedMethodsInfos() {
        if (generalizedMethodInfoJsonPojos.isEmpty()) {
            final List<GeneralizedMethodInfoJsonPojo> all = generalizedMethodInfoComponent.getAll();
            generalizedMethodInfoJsonPojos.addAll(all);
        }
    }
}
