package com.nir.beans;

import com.nir.utils.math.method.ButchersTableJsonPojo;
import com.nir.utils.math.method.MethodInfoJsonPojo;
import com.nir.utils.math.method.Method;

import java.util.List;
import java.util.stream.Collectors;

public class MethodComponent {
    public List<Method> createOf(List<MethodInfoJsonPojo> methodsInfos) {
        final List<Method> collect =
            methodsInfos.stream()
                .filter(this::butchersTableHasNoParams)
                .map(MethodCreator::create)
                .collect(Collectors.toList());
        return collect;
    }

    private boolean butchersTableHasNoParams(MethodInfoJsonPojo methodInfoJsonPojo) {
        final ButchersTableJsonPojo pojo = methodInfoJsonPojo.getButchersTableJsonPojo();
        final boolean hasNumericExpressions = pojo.hasNumericExpressions();
        final boolean hasNoExpression = pojo.hasNoExpression();
        return hasNumericExpressions || hasNoExpression;
    }
}
