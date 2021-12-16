package com.nir.beans.method

import com.nir.beans.Beans.methodConverterComponent
import com.nir.beans.Beans.methodInfoComponent
import com.nir.beans.Beans.hardcodedMethodComponent
import com.nir.beans.method.generalized.MethodInfoJsonPojo

object Methods {
    private val methodInfoComponent: MethodInfoComponent = methodInfoComponent()

    private val generalizedMethodComponent = methodConverterComponent()
    private val hardcodedMethodComponent = hardcodedMethodComponent()

    private val hardcodedMethods: MutableList<Method> = ArrayList()
    private val allMethods: MutableList<Method> = ArrayList()
    private val allMethodsJsonPojos: MutableList<MethodInfoJsonPojo> = ArrayList()

    init {

        if (hardcodedMethods.isEmpty()) {
            allMethods.addAll(hardcodedMethodComponent.getMethods)
        }

        allMethodsJsonPojos.addAll(methodInfoComponent.getMethods)
        allMethodsJsonPojos.forEach { println(it) }
    }

    @JvmStatic
    fun getByName(name: String): Method {
        val all = allMethods.filter { it.name == name }
        if (all.isNotEmpty()) {
            return all.first()
        }

        val pojo = allMethodsJsonPojos.first { it.name == name }
        val method = generalizedMethodComponent.create(arrayListOf(pojo))[0]
        if (!allMethods.contains(method)) {
            allMethods.add(method)
        }

        return method
    }
}