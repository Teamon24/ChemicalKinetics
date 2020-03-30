package com.nir.utils.math.flow

import com.nir.utils.ArrayUtils
import com.nir.utils.math.F
import com.nir.utils.math.R
import com.nir.utils.math.RungeKutta4Core
import com.nir.utils.math.RungeKuttaCore
import com.nir.utils.math.T
import com.nir.utils.math.plus
import com.nir.utils.math.times
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

abstract class MethodWithFlow {
    abstract operator fun invoke(f: F,
                                 r0: Array<Double>,
                                 t0: Double,
                                 N: Int,
                                 dt: Double): Flow<Pair<R, T>>
}

object EulerFlow : MethodWithFlow() {
    override fun invoke(f: F,
                        r0: Array<Double>,
                        t0: Double,
                        N: Int,
                        dt: Double): Flow<Pair<R, T>> {
        val D = r0.size
        var t = t0
        val r = ArrayUtils.twoDimArray(N to D)
        return flow {

            (0 until D).forEach { i -> r[0][i] = r0[i] }

            for (i in 0 until N - 1) {
                r[i + 1] = r[i] + dt * f(r[i], t)

                if (i % 2 == 0) delay(1)

                emit(r[i + 1] to t)
                t += dt
            }
        }
    }


}

class RungeKuttaFlow(order: Int): MethodWithFlow() {
    private val core: RungeKuttaCore = when (order) {
        4 -> RungeKutta4Core()
        else -> throw RuntimeException("For $order no Runge-Kutta method")
    }

    override operator fun invoke(f: F,
                                 r0: Array<Double>,
                                 t0: Double,
                                 N: Int,
                                 dt: Double): Flow<Pair<R, T>>
    {
        val D = r0.size
        var t = t0
        val r = ArrayUtils.twoDimArray(N to D)
        return flow {
            val k = ArrayUtils.twoDimArray(4 to D)

            (0 until D).forEach { i -> r[0][i] = r0[i] }

            val half: Double = dt / 2.0
            val sixth = dt / 6.0

            for (i in 0 until N - 1) {
                r[i + 1] = r[i] + core(k, f, t, dt, arrayOf(half, sixth), r[i])

                if (i % 2 == 0) delay(1)

                emit(r[i + 1] to t)
                t += dt
            }
        }
    }
}



