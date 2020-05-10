package com.nir.beans;

import com.nir.utils.math.method.generalized.GeneralizedAdamsBashforthMethod;
import com.nir.utils.math.method.generalized.GeneralizedAdamsMoultonMethod;
import com.nir.utils.math.method.jsonPojos.AdamsBashforthGeneralizedMethodJsonPojo;
import com.nir.utils.math.method.jsonPojos.AdamsMoultonGeneralizedMethodJsonPojo;
import com.nir.utils.math.method.ButchersTable;
import com.nir.utils.math.method.jsonPojos.ButchersTableJsonPojo;
import com.nir.utils.math.method.generalized.GeneralizedExplicitRungeKuttaMethod;
import com.nir.utils.math.method.jsonPojos.ExplicitRKGeneralizedMethodJsonPojo;
import com.nir.utils.math.method.Method;
import com.nir.utils.math.method.generalized.GeneralizedMethodInfoJsonPojo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GeneralizedMethodComponent {
    public List<Method> create(List<GeneralizedMethodInfoJsonPojo> methodsInfos) {
        final List<Method> collect =
            methodsInfos.stream()
                .map(this::create)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return collect;
    }

    private Method create(GeneralizedMethodInfoJsonPojo generalizedMethodInfoJsonPojo) {
        if (generalizedMethodInfoJsonPojo.getClass() == ExplicitRKGeneralizedMethodJsonPojo.class) {
            final ExplicitRKGeneralizedMethodJsonPojo rungeKutta = (ExplicitRKGeneralizedMethodJsonPojo) generalizedMethodInfoJsonPojo;
            return create(rungeKutta);
        }

        if (generalizedMethodInfoJsonPojo.getClass() == AdamsBashforthGeneralizedMethodJsonPojo.class) {
            final AdamsBashforthGeneralizedMethodJsonPojo adamsBashforth = (AdamsBashforthGeneralizedMethodJsonPojo) generalizedMethodInfoJsonPojo;
            return create(adamsBashforth);
        }

        if (generalizedMethodInfoJsonPojo.getClass() == AdamsMoultonGeneralizedMethodJsonPojo.class) {
            final AdamsMoultonGeneralizedMethodJsonPojo adamsMoulton = (AdamsMoultonGeneralizedMethodJsonPojo) generalizedMethodInfoJsonPojo;
            return create(adamsMoulton);
        }

        final String template = "Has no case for subtype '%s' of type '%s'";
        final String message = String.format(template, generalizedMethodInfoJsonPojo.getClass(), GeneralizedMethodInfoJsonPojo.class);
        throw new IllegalArgumentException(message);
    }


    private Method create(ExplicitRKGeneralizedMethodJsonPojo explicitRKMethodJsonPojo)  {
        if (isValid(explicitRKMethodJsonPojo)) {
            final ButchersTableJsonPojo butchersTableJsonPojo = explicitRKMethodJsonPojo.getButchersTableJsonPojo();
            final ButchersTable butchersTable = ButcherTableConverter.convert(butchersTableJsonPojo);
            final int order = explicitRKMethodJsonPojo.getOrder();
            final int stages = explicitRKMethodJsonPojo.getStages();
            final String name = explicitRKMethodJsonPojo.getName();
            final GeneralizedExplicitRungeKuttaMethod explicitRKMethod = new GeneralizedExplicitRungeKuttaMethod(stages, order, butchersTable, name);
            return explicitRKMethod;
        }
        return null;
    }

    private Method create(AdamsBashforthGeneralizedMethodJsonPojo adamsBashforthMethodJsonPojo)  {
        final String name = adamsBashforthMethodJsonPojo.getName();
        final int order = adamsBashforthMethodJsonPojo.getK();
        final Object[] betta = adamsBashforthMethodJsonPojo.getBetta();
        final Object c = adamsBashforthMethodJsonPojo.getC();
        final GeneralizedAdamsBashforthMethod adamsBashforthMethod = new GeneralizedAdamsBashforthMethod(name, order, betta, c);
        return adamsBashforthMethod;
    }

    private Method create(AdamsMoultonGeneralizedMethodJsonPojo adamsMoultonMethodJsonPojo)  {
        final String name = adamsMoultonMethodJsonPojo.getName();
        final int order = adamsMoultonMethodJsonPojo.getK();
        final Object[] betta = adamsMoultonMethodJsonPojo.getBetta();
        final Object c = adamsMoultonMethodJsonPojo.getC();
        final GeneralizedAdamsMoultonMethod adamsMoultonMethod = new GeneralizedAdamsMoultonMethod(name, order, betta, c);
        return adamsMoultonMethod;
    }

    private boolean isValid(ExplicitRKGeneralizedMethodJsonPojo explicitRKMethodJsonPojo) {
        final ButchersTableJsonPojo pojo = explicitRKMethodJsonPojo.getButchersTableJsonPojo();
        final boolean hasNoVariables = !pojo.hasVariablesExpression();
        final boolean isNotAdaptive = !pojo.isAdaptive();
        return hasNoVariables && isNotAdaptive;
    }
}
