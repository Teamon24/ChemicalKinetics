package com.nir.utils.math.method.deprecated

import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.method.D
import com.nir.utils.math.method.F
import com.nir.utils.math.method.N
import com.nir.utils.math.method.X
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.dX
import com.nir.utils.math.plus
import com.nir.utils.math.times
import kotlin.properties.Delegates

interface RungeKuttaCore {
    fun init(d: D, dx: dX)

    operator fun invoke(f: F, y: Y, x: X, dx: dX): Array<Double>
}

class RungeKutta4Core: RungeKuttaCore {
    private val methodOrder = 4

    private lateinit var k: Array<Array<Double>>
    private var sixth by Delegates.notNull<Double>()
    private var half by Delegates.notNull<Double>()

    override fun init(d: D, dx: dX) {
        k = init(methodOrder, d)
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

private fun init(n: N, d: D) = ArrayUtils.twoDimArray(n to d)



