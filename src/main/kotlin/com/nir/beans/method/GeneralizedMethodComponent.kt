package com.nir.beans.method

import com.nir.beans.reaction.parsing.ButcherTableConverter.convert
import com.nir.beans.method.generalized.GeneralizedMethodInfoJsonPojo
import java.util.Objects
import java.util.stream.Collectors
import com.nir.beans.method.jsonPojos.ExplicitRKGeneralizedMethodJsonPojo
import com.nir.beans.method.jsonPojos.AdamsBashforthGeneralizedMethodJsonPojo
import com.nir.beans.method.jsonPojos.AdamsMoultonGeneralizedMethodJsonPojo
import com.nir.beans.method.generalized.GeneralizedExplicitRungeKuttaMethod
import com.nir.beans.method.generalized.GeneralizedAdamsBashforthMethod
import com.nir.beans.method.generalized.GeneralizedAdamsMoultonMethod
import java.lang.IllegalArgumentException

class GeneralizedMethodComponent {
    fun create(methodsInfoPojos: List<GeneralizedMethodInfoJsonPojo>): List<Method?> {
        return methodsInfoPojos.stream()
            .map { pojos -> create(pojos) }
            .filter { obj: Method? -> Objects.nonNull(obj) }
            .collect(Collectors.toList())
    }

    private fun create(pojo: GeneralizedMethodInfoJsonPojo): Method? {
        if (pojo.javaClass == ExplicitRKGeneralizedMethodJsonPojo::class.java) {
            val rungeKutta = pojo as ExplicitRKGeneralizedMethodJsonPojo
            return create(rungeKutta)
        }

        if (pojo.javaClass == AdamsBashforthGeneralizedMethodJsonPojo::class.java) {
            val adamsBashforth = pojo as AdamsBashforthGeneralizedMethodJsonPojo
            return create(adamsBashforth)
        }

        if (pojo.javaClass == AdamsMoultonGeneralizedMethodJsonPojo::class.java) {
            val adamsMoulton = pojo as AdamsMoultonGeneralizedMethodJsonPojo
            return create(adamsMoulton)
        }

        val template = "Has no case for subtype '%s' of type '%s'"
        val message =
            String.format(template, pojo.javaClass, GeneralizedMethodInfoJsonPojo::class.java)
        throw IllegalArgumentException(message)
    }

    private fun create(explicitRKMethodJsonPojo: ExplicitRKGeneralizedMethodJsonPojo): Method? {
        if (isValid(explicitRKMethodJsonPojo)) {
            val butchersTableJsonPojo = explicitRKMethodJsonPojo.butchersTableJsonPojo
            val butchersTable = convert(butchersTableJsonPojo)
            val order = explicitRKMethodJsonPojo.order
            val stages = explicitRKMethodJsonPojo.stages
            val name = explicitRKMethodJsonPojo.name
            return GeneralizedExplicitRungeKuttaMethod(stages, order, butchersTable, name)
        }
        return null
    }

    private fun create(adamsBashforthMethodJsonPojo: AdamsBashforthGeneralizedMethodJsonPojo): Method {
        val name = adamsBashforthMethodJsonPojo.name
        val order = adamsBashforthMethodJsonPojo.k
        val betta: Array<Any> = adamsBashforthMethodJsonPojo.betta
        val c = adamsBashforthMethodJsonPojo.C
        return GeneralizedAdamsBashforthMethod(name, order, betta, c)
    }

    private fun create(adamsMoultonMethodJsonPojo: AdamsMoultonGeneralizedMethodJsonPojo): Method {
        val name = adamsMoultonMethodJsonPojo.name
        val order = adamsMoultonMethodJsonPojo.k
        val betta: Array<Any> = adamsMoultonMethodJsonPojo.betta
        val c = adamsMoultonMethodJsonPojo.C
        return GeneralizedAdamsMoultonMethod(name, order, betta, c)
    }

    private fun isValid(explicitRKMethodJsonPojo: ExplicitRKGeneralizedMethodJsonPojo): Boolean {
        val pojo = explicitRKMethodJsonPojo.butchersTableJsonPojo
        val hasNoVariables = !pojo.hasVariablesExpression()
        val isNotAdaptive = !pojo.isAdaptive()
        return hasNoVariables && isNotAdaptive
    }
}