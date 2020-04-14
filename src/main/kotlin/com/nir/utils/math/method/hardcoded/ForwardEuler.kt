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
import com.nir.utils.math.method.times

object ForwardEuler : HardcodedMethod() {
    override val name: String get() = "Forward Euler (Hardcoded)"

    override fun setUp(initialPoint: InitialPoint, computationConfig: ComputationConfigs): Method {
        return this
    }

    override fun invoke(f: F,
                        x0: X,
                        y0: Y,
                        dx: dX,
                        n: N): Array<Y> {
        val d = y0.size
        val r = ArrayUtils.twoDimArray(n to d)
        var x = x0

        (0 until d).forEach { i -> r[0][i] = y0[i] }

        for (i in 0 until n - 1) {
            r[i + 1] = this(f, x, r[i], dx)
            x += dx
        }

        return r
    }

    override fun invoke(f: F, x: X, y: Y, dx: dX): Y {
        return y + dx * f(x, y)
    }
}