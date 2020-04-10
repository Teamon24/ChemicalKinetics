package com.nir.utils.math.method.deprecated

import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.method.D
import com.nir.utils.math.method.F
import com.nir.utils.math.method.Method
import com.nir.utils.math.method.N
import com.nir.utils.math.method.X
import com.nir.utils.math.method.X0
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.dX
import com.nir.utils.math.plus

/**
 * Метод решения системы уравенений "Рунге-Кутта".
 */
class RungeKutta(private val order: Int): DeprecatedMethod() {

    private val core: RungeKuttaCore = when (order) {
        4 -> RungeKutta4Core()
        else -> throw RuntimeException("For order='$order' no Runge-Kutta method")
    }
    override val name: String get() = "Runge-Kutta ${order}-order"

    override fun set(dx: dX) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun set(d: D, dx: dX) {
        core.init(d, dx)
    }

    override operator fun invoke(f: F,
                                 y0: Y,
                                 x0: X0,
                                 dx: dX,
                                 n: N): Array<Y> {
        val d = y0.size
        val r = init(n, d)

        var t = x0

        (0 until d).forEach { i -> r[0][i] = y0[i] }

        for (i in 0 until n - 1) {
            r[i + 1] = this(f, r[i], t, dx)
            t += dx
        }

        return r
    }

    override fun invoke(f: F, y: Y, x: X, dx: dX): Y {
        return y + core(f, y, x, dx)
    }

    private fun init(n: N, d: D) = ArrayUtils.twoDimArray(n to d)
}