package com.nir.beans;

import com.google.common.collect.Lists;
import com.nir.utils.math.method.AdamsBashforthMethod;
import com.nir.utils.math.method.AdamsBashforthMethods;
import com.nir.utils.math.method.AdamsMoultonMethod;
import com.nir.utils.math.method.jsonPojos.AdamsBashforthMethodJsonPojo;
import com.nir.utils.math.method.jsonPojos.AdamsMoultonMethodJsonPojo;
import com.nir.utils.math.method.ButchersTable;
import com.nir.utils.math.method.jsonPojos.ButchersTableJsonPojo;
import com.nir.utils.math.method.ExplicitRungeKuttaMethod;
import com.nir.utils.math.method.jsonPojos.ExplicitRKMethodJsonPojo;
import com.nir.utils.math.method.Method;
import com.nir.utils.math.method.MethodInfoJsonPojo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodComponent {
    public List<Method> create(List<MethodInfoJsonPojo> methodsInfos) {
        final List<Method> collect =
            methodsInfos.stream()
                .map(this::create)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return collect;
    }

    private Method create(MethodInfoJsonPojo methodInfoJsonPojo) {
        if (methodInfoJsonPojo.getClass() == ExplicitRKMethodJsonPojo.class) {
            final ExplicitRKMethodJsonPojo rungeKutta = (ExplicitRKMethodJsonPojo) methodInfoJsonPojo;
            return create(rungeKutta);
        }

        if (methodInfoJsonPojo.getClass() == AdamsBashforthMethodJsonPojo.class) {
            final AdamsBashforthMethodJsonPojo adamsBashforth = (AdamsBashforthMethodJsonPojo) methodInfoJsonPojo;
            return create(adamsBashforth);
        }

        if (methodInfoJsonPojo.getClass() == AdamsMoultonMethodJsonPojo.class) {
            final AdamsMoultonMethodJsonPojo adamsMoulton = (AdamsMoultonMethodJsonPojo) methodInfoJsonPojo;
            return create(adamsMoulton);
        }

        final String template = "Has no case for subtype '%s' of type '%s'";
        final String message = String.format(template, methodInfoJsonPojo.getClass(), MethodInfoJsonPojo.class);
        throw new IllegalArgumentException(message);
    }


    private Method create(ExplicitRKMethodJsonPojo explicitRKMethodJsonPojo)  {
        if (isValid(explicitRKMethodJsonPojo)) {
            final ButchersTableJsonPojo butchersTableJsonPojo = explicitRKMethodJsonPojo.getButchersTableJsonPojo();
            final ButchersTable butchersTable = ButcherTableConverter.convert(butchersTableJsonPojo);
            final int order = explicitRKMethodJsonPojo.getOrder();
            final int stages = explicitRKMethodJsonPojo.getStages();
            final String name = explicitRKMethodJsonPojo.getName();
            final ExplicitRungeKuttaMethod explicitRKMethod = new ExplicitRungeKuttaMethod(stages, order, butchersTable, name);
            return explicitRKMethod;
        }
        return null;
    }

    private Method create(AdamsBashforthMethodJsonPojo adamsBashforthMethodJsonPojo)  {
        final String name = adamsBashforthMethodJsonPojo.getName();
        final int order = adamsBashforthMethodJsonPojo.getK();
        final Object[] betta = adamsBashforthMethodJsonPojo.getBetta();
        final Object c = adamsBashforthMethodJsonPojo.getC();
        final AdamsBashforthMethod adamsBashforthMethod = new AdamsBashforthMethod(name, order, betta, c);
        AdamsBashforthMethods.set(order, adamsBashforthMethod);
        return adamsBashforthMethod;
    }

    private Method create(AdamsMoultonMethodJsonPojo adamsMoultonMethodJsonPojo)  {
        final String name = adamsMoultonMethodJsonPojo.getName();
        final int order = adamsMoultonMethodJsonPojo.getK();
        final Object[] betta = adamsMoultonMethodJsonPojo.getBetta();
        final Object c = adamsMoultonMethodJsonPojo.getC();
        final AdamsMoultonMethod adamsMoultonMethod = new AdamsMoultonMethod(name, order, betta, c);
        return adamsMoultonMethod;
    }

    private boolean isValid(ExplicitRKMethodJsonPojo explicitRKMethodJsonPojo) {
        final ButchersTableJsonPojo pojo = explicitRKMethodJsonPojo.getButchersTableJsonPojo();
        final boolean hasNoVariables = !pojo.hasVariablesExpression();
        final boolean isNotAdaptive = !pojo.isAdaptive();
        return hasNoVariables && isNotAdaptive;
    }
}
