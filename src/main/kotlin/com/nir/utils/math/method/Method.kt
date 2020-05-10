package com.nir.utils.math.method

import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint

val emptyFunc = { _: X, _: Y -> 0.0 }

/**
 * Метод решения системы уравнений.
 */
abstract class Method(open val name: String) {

    abstract fun setUp(initialPoint: InitialPoint,
                       computationConfig: ComputationConfigs): Method

    abstract operator fun invoke(f: F,
                                 x0: X,
                                 y0: Y,
                                 dx: dX,
                                 n: N): Pair<Xs, Ys>

    abstract operator fun invoke(f: F,
                                 x: X,
                                 y: Y,
                                 dx: dX): Y

    fun initXs(n: N, x0: X): DoubleArray {
        val doubleArray = DoubleArray(n + 1) { 0.0 }
        doubleArray[0] = x0
        return doubleArray
    }

    fun initYs(n: N, y0: Y): Array<Array<Double>> {
        val d = y0.size
        val ys = ArrayUtils.twoDimArray(n + 1 to d)
        ys[0] = y0
        return ys
    }
}
