package com.nir.beans.method.hardcoded

import com.nir.utils.math.D
import com.nir.utils.math.F
import com.nir.utils.math.X
import com.nir.utils.math.Y
import com.nir.utils.math.dX
import com.nir.utils.plus
import com.nir.utils.times
import kotlin.properties.Delegates

class RungeKutta4Core: RungeKuttaCore {
    private val stages = 4

    private lateinit var k: Array<Array<Double>>
    private var sixth by Delegates.notNull<Double>()
    private var half by Delegates.notNull<Double>()

    override fun setUp(d: D, dx: dX) {
        k = init(stages, d)
        half = dx / 2.0
        sixth = dx / 6.0
    }

    override operator fun invoke(f: F, y: Y, x: X, dx: dX): Array<Double> {
        k[0] = f(x, y)
        k[1] = f(x + half, y + half * k[0])
        k[2] = f(x + half, y + half * k[1])
        k[3] = f(x + dx, y + dx * k[2])

        val kSum = sixth * (k[0] + 2 * k[1] + 2 * k[2] + k[3])
        return kSum
    }
}