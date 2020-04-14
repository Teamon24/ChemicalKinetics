package com.nir.utils.math.method.hardcoded

import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.method.D
import com.nir.utils.math.method.F
import com.nir.utils.math.method.automatized.Method
import com.nir.utils.math.method.N
import com.nir.utils.math.method.X
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.dX
import com.nir.utils.math.plus

/**
 * Метод решения системы уравенений "Рунге-Кутта".
 */
class RungeKutta(private val order: Int): HardcodedMethod() {

    private val core: RungeKuttaCore = when (order) {
        4 -> RungeKutta4Core()
        5 -> RungeKutta5Core()
        else -> throw RuntimeException("For order='$order' no Runge-Kutta method")
    }
    override val name: String get() = "Runge-Kutta ${order}-order (Hardcoded)"

    override fun setUp(initialPoint: InitialPoint, computationConfig: ComputationConfigs): Method {
        core.setUp(initialPoint.y0.size, computationConfig.dx)
        return this
    }

    override operator fun invoke(f: F,
                                 x0: X,
                                 y0: Y,
                                 dx: dX,
                                 n: N): Array<Y> {
        val d = y0.size
        val r = init(n, d)

        var x = x0

        (0 until d).forEach { i -> r[0][i] = y0[i] }

        for (i in 0 until n - 1) {
            r[i + 1] = this(f, x, r[i], dx)
            x += dx
        }

        return r
    }

    override fun invoke(f: F, x: X, y: Y, dx: dX): Y {
        return y + core(f, y, x, dx)
    }

    private fun init(n: N, d: D) = ArrayUtils.twoDimArray(n to d)
}