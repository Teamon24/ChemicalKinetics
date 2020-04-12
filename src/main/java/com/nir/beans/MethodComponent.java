package com.nir.beans;

import com.nir.utils.math.method.AdamsBashforthMethodJsonPojo;
import com.nir.utils.math.method.AdamsMoultonMethodJsonPojo;
import com.nir.utils.math.method.ButchersTable;
import com.nir.utils.math.method.ButchersTableJsonPojo;
import com.nir.utils.math.method.ExplicitRungeKutta;
import com.nir.utils.math.method.ExplicitRKMethodJsonPojo;
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
            ExplicitRungeKutta explicit = new ExplicitRungeKutta(stages, order, butchersTable, name);
            return Method.from(explicit);
        }
        return null;
    }

    private Method create(AdamsBashforthMethodJsonPojo adamsBashforthMethodJsonPojo)  {
        return null;
    }

    private Method create(AdamsMoultonMethodJsonPojo adamsMoultonMethodJsonPojo)  {
        return null;
    }

    private boolean isValid(ExplicitRKMethodJsonPojo explicitRKMethodJsonPojo) {
        final ButchersTableJsonPojo pojo = explicitRKMethodJsonPojo.getButchersTableJsonPojo();
        final boolean hasNoVariables = !pojo.hasVariablesExpression();
        final boolean isNotAdaptive = !pojo.isAdaptive();
        return hasNoVariables && isNotAdaptive;
    }
}
