package com.nir.utils.math.method.hardcoded

import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.method.D
import com.nir.utils.math.method.F
import com.nir.utils.math.method.Method
import com.nir.utils.math.method.N
import com.nir.utils.math.method.X
import com.nir.utils.math.method.Xs
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.Ys
import com.nir.utils.math.method.dX
import com.nir.utils.plus

/**
 * Метод решения системы уравенений "Рунге-Кутта".
 */
class RungeKutta(order: Int): HardcodedMethod("Runge-Kutta ${order}-order") {

    private val core: RungeKuttaCore =
            when (order) {
                4 -> RungeKutta4Core()
                5 -> RungeKutta5Core()
                else -> throw RuntimeException("For order='$order' no Runge-Kutta method")
            }

    override fun setUp(initialPoint: InitialPoint,
                       computationConfig: ComputationConfigs): Method
    {
        core.setUp(initialPoint.y0.size, computationConfig.dx)
        return this
    }

    override operator fun invoke(f: F,
                                 x0: X,
                                 y0: Y,
                                 dx: dX,
                                 n: N): Pair<Xs, Ys>
    {
        val ys = initYs(n, y0)
        val xs = initXs(n, x0)
        var x = x0

        for (i in 0 until n - 1) {
            x += dx
            xs[i + 1] = x
            ys[i + 1] = this(f, x, ys[i], dx)
        }

        return xs to ys
    }

    override fun invoke(f: F, x: X, y: Y, dx: dX): Y {
        return y + core(f, y, x, dx)
    }

    private fun init(n: N, d: D) = ArrayUtils.twoDimArray(n to d)
}