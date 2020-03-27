package com.nir.ui.utils.math

import com.nir.ui.utils.ArrayUtils
import com.nir.ui.utils.F
import com.nir.ui.utils.R
import com.nir.ui.utils.T
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*


class RungeKuttaFlow(order: Int) {
    private val core: RungeKuttaCore = when (order) {
        4 -> RungeKutta4Core()
        else -> throw RuntimeException("For $order no Runge-Kutta method")
    }

    fun flow(f: F,
             r0: Array<Double>,
             t0: Double,
             N: Int,
             dt: Double): Flow<Pair<R, T>>
    {
        return flow {
            val D = r0.size
            val r = ArrayUtils.twoDimArray(N to D)
            val k = ArrayUtils.twoDimArray(4 to D)
            var t = t0

            (0 until D).forEach { i -> r[0][i] = r0[i] }

            val half: Double = dt / 2.0
            val sixth = dt / 6.0

            for (i in 0 until N - 1) {
                r[i + 1] = r[i] + core(k, f, t, dt, arrayOf(half, sixth), r[i])
//                println("i:$i, x:${r[i + 1][0]}, y:${r[i + 1][1]}, z:${r[i + 1][2]} t: $t")
                emit(r[i + 1] to t)
                t += dt
            }
        }
    }
}



