package com.nir.utils.math

import com.nir.utils.ArrayUtils


/**
 * Метод решения системы уравнений.
 */
sealed class Method {
    abstract operator fun invoke(system: System,
                                 r0: R,
                                 t0: T,
                                 dt: T,
                                 N: Int): Array<R>

    abstract operator fun invoke(system: System,
                                 r: R,
                                 t: T,
                                 dt: T): R
}

object Euler : Method() {
    override fun invoke(system: System,
                        r0: R,
                        t0: T,
                        dt: T,
                        N: Int): Array<Array<Double>> {
        val D = r0.size
        val r = ArrayUtils.twoDimArray(N to D)
        var t = t0

        (0 until D).forEach { i -> r[0][i] = r0[i] }

        for (i in 0 until N - 1) {
            r[i + 1] = r[i] + dt * system(t, r[i])
            t += dt
        }

        return r
    }

    override fun invoke(system: System, r: R, t: T, dt: T): R {
        return r + dt * system(t, r)
    }
}


/**
 * Метод решения системы уравенений "Рунге-Кутта".
 */
class RungeKutta(order: Int, initialData: InitialData): Method() {

    private val core: RungeKuttaCore = when (order) {
        4 -> RungeKutta4Core(initialData)
        else -> throw RuntimeException("For order='$order' no Runge-Kutta method")
    }

    override operator fun invoke(system: System,
                                 r0: R,
                                 t0: T,
                                 dt: T,
                                 N: Int): Array<Array<Double>> {
        val D = r0.size
        val r = init(N, D)

        var t = t0

        (0 until D).forEach { i -> r[0][i] = r0[i] }

        for (i in 0 until N - 1) {
            r[i + 1] = this(system, r[i], t, dt)
            t += dt
        }

        return r
    }

    override fun invoke(system: System, r: R, t: T, dt: T): R {
        return r + core(system, r, t, dt)
    }

    private fun init(N: Int, D: Int) = ArrayUtils.twoDimArray(N to D)
}

