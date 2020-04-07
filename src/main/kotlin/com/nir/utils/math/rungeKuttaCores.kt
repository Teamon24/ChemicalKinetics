package com.nir.utils.math

import com.nir.beans.k
import kotlin.properties.Delegates

interface RungeKuttaCore {
    fun init(dimension: Int, dt: T)

    operator fun invoke(system: System,
                        r: R,
                        t: Double,
                        dt: Double): Array<Double>
}

class RungeKutta4Core: RungeKuttaCore {
    private val methodOrder = 4

    private lateinit var k: Array<k>
    private var sixth by Delegates.notNull<Double>()
    private var half by Delegates.notNull<Double>()

    override fun init(dimension: Int, dt: T) {
        k = init(methodOrder, dimension)
        half = dt / 2.0
        sixth = dt / 6.0
    }

    override operator fun invoke(system: System,
                                 r: R,
                                 t: Double,
                                 dt: Double): Array<Double> {
        k[0] = system(t, r)
        k[1] = system(t + half, r + half * k[0])
        k[2] = system(t + half, r + half * k[1])
        k[3] = system(t + dt, r + dt * k[2])

        val kSum = sixth * (k[0] + 2 * k[1] + 2 * k[2] + k[3])
        return kSum
    }
}

private fun init(N: Int, D: Int) = ArrayUtils.twoDimArray(N to D)



