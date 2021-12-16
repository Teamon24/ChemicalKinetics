package com.nir.beans.method

import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.D
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.F
import com.nir.utils.math.N
import com.nir.utils.math.X
import com.nir.utils.math.Xs
import com.nir.utils.math.Y
import com.nir.utils.math.Ys
import com.nir.utils.math.dX

val emptyFunc = { _: X, _: Y -> 0.0 }

/**
 * Метод решения системы уравнений.
 */
abstract class Method(open val name: String) {

    /**
     * Метод следует вызывать сразу после получения объекта [Method],
     * чтобы инициализировать необходимые переменные: например переменную [GeneralizedExplicitRungeKuttaMethod.K]
     */
    abstract fun setUp(dx: X, d: D): Method

    abstract operator fun invoke(f: F,
                                 x0: X,
                                 y0: Y,
                                 dx: dX,
                                 n: N
    ): Pair<Xs, Ys>
    abstract operator fun invoke(f: F,
                                 x: X,
                                 y: Y,
                                 dx: dX
    ): Y

    fun initXs(n: N, x0: X): Xs {
        val doubleArray = DoubleArray(n) { 0.0 }
        doubleArray[0] = x0
        return doubleArray
    }

    fun initYs(n: N, y0: Y): Ys {
        val d = y0.size
        val ys = ArrayUtils.twoDimArray(n to d)
        ys[0] = y0
        return ys
    }

}
