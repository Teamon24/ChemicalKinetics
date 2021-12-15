package com.nir.beans.method

import com.nir.beans.Beans.generalizedMethodComponent
import com.nir.beans.Beans.generalizedMethodInfoComponent
import com.nir.beans.Beans.hardcodedMethodComponent
import com.nir.beans.method.generalized.GeneralizedMethodInfoJsonPojo

object Methods {
    private val generalizedMethodInfoComponent: GeneralizedMethodInfoComponent = generalizedMethodInfoComponent()
    private val generalizedMethodComponent = generalizedMethodComponent()
    private val hardcodedMethodComponent = hardcodedMethodComponent()
    private val generalizedMethods: MutableList<Method> = ArrayList()
    private val hardcodedMethods: MutableList<Method> = ArrayList()
    private val allMethods: MutableList<Method> = ArrayList()
    private val generalizedMethodInfoJsonPojos: MutableList<GeneralizedMethodInfoJsonPojo> = ArrayList()

    fun getAll(): List<Method> {
        initGeneralizedMethods()
        initHardcodedMethods()
        initAllMethods()
        return allMethods
    }


    fun getNames(): List<String> {
        return getAll().map { it.name }
    }

    @JvmStatic
    fun getByName(name: String): Method {
        val all = getAll()
        val first =
            all.stream().filter { it.name == name }.findFirst()
        return first.get()
    }

    private fun initAllMethods() {
        if (allMethods.isEmpty()) {
            allMethods.addAll(hardcodedMethods)
            allMethods.addAll(generalizedMethods)
        }
    }

    private fun initHardcodedMethods() {
        if (hardcodedMethods.isEmpty()) {
            val created = hardcodedMethodComponent.getMethods
            hardcodedMethods.addAll(created)
        }
    }

    private fun initGeneralizedMethods() {
        initGeneralizedMethodsInfos()
        if (generalizedMethods.isEmpty()) {
            val created = generalizedMethodComponent.create(generalizedMethodInfoJsonPojos)
            generalizedMethods.addAll(created.filterNotNull())
        }
    }

    private fun initGeneralizedMethodsInfos() {
        if (generalizedMethodInfoJsonPojos.isEmpty()) {
            val all = generalizedMethodInfoComponent.getMethods
            generalizedMethodInfoJsonPojos.addAll(all)
        }
    }
}