package com.nir.utils.math

import com.nir.utils.ArrayUtils

/**
 * Метод решения системы уравнений.
 */
sealed class Method {
    abstract operator fun invoke(f: F,
                                 r0: Array<Double>,
                                 t0: Double,
                                 N: Int,
                                 dt: Double): Array<Array<Double>>
}

object Euler : Method() {
    override fun invoke(f: F,
                        r0: Array<Double>,
                        t0: Double,
                        N: Int,
                        dt: Double): Array<Array<Double>> {
        val D = r0.size
        val r = ArrayUtils.twoDimArray(N to D)
        var t = t0

        (0 until D).forEach { i -> r[0][i] = r0[i] }

        for (i in 0 until N - 1) {
            r[i + 1] = r[i] + dt * f(r[i], t)
            t += dt
        }

        return r
    }
}

/**
 * Метод решения системы уравенений "Рунге-Кутта".
 */
class RungeKutta(order: Int): Method() {
    private val core: RungeKuttaCore = when (order) {
        4 -> RungeKutta4Core()
        else -> throw RuntimeException("For $order no Runge-Kutta method")
    }

    override operator fun invoke(f: F,
                                 r0: Array<Double>,
                                 t0: Double,
                                 N: Int,
                                 dt: Double): Array<Array<Double>> {
        val D = r0.size
        val r = ArrayUtils.twoDimArray(N to D)
        val k = ArrayUtils.twoDimArray(4 to D)
        var t = t0

        (0 until D).forEach { i -> r[0][i] = r0[i] }

        val half: Double = dt / 2.0
        val sixth = dt / 6.0

        for (i in 0 until N - 1) {
            r[i + 1] = r[i] + core(k, f, t, dt, arrayOf(half, sixth), r[i])
            t += dt
        }

        return r
    }
}



