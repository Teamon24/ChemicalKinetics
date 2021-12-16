package com.nir.utils.math.solution

import com.nir.beans.method.Method
import com.nir.beans.method.Methods
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.F
import com.nir.utils.math.InitialPoint
import java.util.function.Consumer

class MethodStep(
    private val system: F,
    private val initialPoint: InitialPoint,
    private val computationConfigs: ComputationConfigs
) {
    fun method(methodName: String): DataSetsStep {
        val method = Methods.getByName(methodName)
        val dimension = initialPoint.y0.size
        val dx = computationConfigs.dx
        method.setUp(dx, dimension)
        return DataSetsStep(method, computationConfigs, system, initialPoint)
    }

    fun method(methodName: String, additionalAction: Consumer<Method>): DataSetsStep {
        val method = Methods.getByName(methodName)
        val dimension = initialPoint.y0.size
        val dx = computationConfigs.dx
        method.setUp(dx, dimension)
        additionalAction.accept(method)
        return DataSetsStep(method, computationConfigs, system, initialPoint)
    }

}