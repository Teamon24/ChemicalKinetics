package com.nir.beans.method.hardcoded

import com.nir.beans.method.Methods
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.F
import com.nir.beans.method.Method

class AdamBashforth2thMethods : AdamBashforthMethod(2, listOf(3/2.0, -1/2.0).reversed())
class AdamBashforth3thMethods : AdamBashforthMethod(3, listOf(23/12.0, -16/12.0, 5/12.0).reversed())
class AdamBashforth4thMethods : AdamBashforthMethod(4, listOf(55/24.0, -59/24.0, 37/24.0, -9/24.0).reversed())
class AdamBashforth5thMethods : AdamBashforthMethod(5, listOf(1901/720.0, -2744/720.0, 2616/720.0, -1274/720.0, 251/720.0).reversed())

object AdamBashforthMethods {

    fun getMethod(order: Int): AdamBashforthMethod {
        return when (order) {
            2 -> AdamBashforth2thMethods()
            3 -> AdamBashforth3thMethods()
            4 -> AdamBashforth4thMethods()
            5 -> AdamBashforth5thMethods()
            else -> throw RuntimeException("No method for order \"${order}\"")
        }
    }

    @JvmStatic
    fun getAccelerationPoints(order: Int,
                              firstAccelerationPointMethodName: String,
                              system: F,
                              initialPoint: InitialPoint,
                              computationConfigs: ComputationConfigs): Array<Array<Double>>
    {
        val firstAccelerationPointMethod = Methods.getByName(firstAccelerationPointMethodName)
        return getAccelerationPoints(order, firstAccelerationPointMethod, system, initialPoint, computationConfigs)
    }


    @JvmStatic
    fun getAccelerationPoints(order: Int,
                              firstAccelerationPointMethod: Method,
                              system: F,
                              initialPoint: InitialPoint,
                              computationConfigs: ComputationConfigs): Array<Array<Double>>
    {
        val dx = computationConfigs.dx
        val x0 = initialPoint.x0
        firstAccelerationPointMethod.setUp(initialPoint, computationConfigs)
        val y0 = initialPoint.y0
        val y1 = firstAccelerationPointMethod(system, x0, y0, dx)
        val pointsSize: Int = arrayOf(y0, y1).size
        val acceleration = Array(order - 1) { Array(initialPoint.y0.size) { 0.0 } }
        acceleration[0] = y1
        for (i in 0 until order - pointsSize) {
            acceleration[i + 1] = getAccelerationPoint(system, dx, x0, y0, firstAccelerationPointMethod, i)
        }
        return acceleration
    }

    private fun getAccelerationPoint(
        system: F,
        dx: Double,
        x0: Double,
        y0: Array<Double>,
        firstAccelerationPointMethod: Method,
        i: Int
    ): Array<Double> {
        val adamBashforthMethod = getMethod(order = i + 2)
        adamBashforthMethod.setFirstAccelerationPointMethod(firstAccelerationPointMethod)
        val (_, ys) = adamBashforthMethod(system, x0, y0, dx, i + 3)
        return ys[i + 2]
    }
}

