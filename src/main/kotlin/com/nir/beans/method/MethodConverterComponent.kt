package com.nir.beans.method

import com.nir.beans.reaction.parsing.ButcherTableConverter.convert
import com.nir.beans.method.generalized.MethodInfoJsonPojo
import java.util.Objects
import java.util.stream.Collectors
import com.nir.beans.method.jsonPojos.ExplicitRKMethodJsonPojo
import com.nir.beans.method.jsonPojos.AdamsBashforthMethodJsonPojo
import com.nir.beans.method.jsonPojos.AdamsMoultonMethodJsonPojo
import com.nir.beans.method.generalized.ExplicitRungeKuttaMethod
import com.nir.beans.method.generalized.AdamsBashforthMethod
import com.nir.beans.method.generalized.AdamsMoultonMethod
import java.lang.IllegalArgumentException

class MethodConverterComponent {
    fun create(methodsInfoPojos: List<MethodInfoJsonPojo>): List<Method> {
        return methodsInfoPojos.stream()
            .map { pojos -> create(pojos) }
            .filter { obj: Method? -> Objects.nonNull(obj) }
            .collect(Collectors.toList())
    }

    private fun create(pojo: MethodInfoJsonPojo): Method {
        if (pojo.javaClass == ExplicitRKMethodJsonPojo::class.java) {
            val rungeKutta = pojo as ExplicitRKMethodJsonPojo
            return create(rungeKutta)
        }

        if (pojo.javaClass == AdamsBashforthMethodJsonPojo::class.java) {
            val adamsBashforth = pojo as AdamsBashforthMethodJsonPojo
            return create(adamsBashforth)
        }

        if (pojo.javaClass == AdamsMoultonMethodJsonPojo::class.java) {
            val adamsMoulton = pojo as AdamsMoultonMethodJsonPojo
            return create(adamsMoulton)
        }

        val template = "Has no case for subtype '%s' of type '%s'"
        val message =
            String.format(template, pojo.javaClass, MethodInfoJsonPojo::class.java)
        throw IllegalArgumentException(message)
    }

    private fun create(explicitRKMethodJsonPojo: ExplicitRKMethodJsonPojo): Method {
        if (isValid(explicitRKMethodJsonPojo)) {
            val butchersTableJsonPojo = explicitRKMethodJsonPojo.butchersTableJsonPojo
            val butchersTable = convert(butchersTableJsonPojo)
            val order = explicitRKMethodJsonPojo.order
            val stages = explicitRKMethodJsonPojo.stages
            val name = explicitRKMethodJsonPojo.name
            return ExplicitRungeKuttaMethod(stages, order, butchersTable, name)
        }

        throw RuntimeException("there is no logic for adaptive and symbolic RK-Methods.")
    }

    private fun create(adamsBashforthMethodJsonPojo: AdamsBashforthMethodJsonPojo): Method {
        val name = adamsBashforthMethodJsonPojo.name
        val order = adamsBashforthMethodJsonPojo.k
        val betta: Array<Any> = adamsBashforthMethodJsonPojo.betta
        val c = adamsBashforthMethodJsonPojo.C
        return AdamsBashforthMethod(name, order, betta, c)
    }

    private fun create(adamsMoultonMethodJsonPojo: AdamsMoultonMethodJsonPojo): Method {
        val name = adamsMoultonMethodJsonPojo.name
        val order = adamsMoultonMethodJsonPojo.k
        val betta: Array<Any> = adamsMoultonMethodJsonPojo.betta
        val c = adamsMoultonMethodJsonPojo.C
        return AdamsMoultonMethod(name, order, betta, c)
    }

    private fun isValid(explicitRKMethodJsonPojo: ExplicitRKMethodJsonPojo): Boolean {
        val pojo = explicitRKMethodJsonPojo.butchersTableJsonPojo
        val hasNoVariables = !pojo.hasVariablesExpression()
        val isNotAdaptive = !pojo.isAdaptive()
        return hasNoVariables && isNotAdaptive
    }
}