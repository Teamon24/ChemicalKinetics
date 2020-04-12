package com.nir.utils.math.method.deprecated

import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialData
import com.nir.utils.math.method.D
import com.nir.utils.math.method.F
import com.nir.utils.math.method.Method
import com.nir.utils.math.method.N
import com.nir.utils.math.method.X
import com.nir.utils.math.method.X0
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.dX
import com.nir.utils.math.plus
import com.nir.utils.math.method.times

object Euler : DeprecatedMethod() {
    override val name: String get() = "Euler"
    override fun set(d: D, dx: dX) {}
    override fun init(initialData: InitialData, computationConfig: ComputationConfigs): Method {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun invoke(f: F,
                        y0: Y,
                        x0: X0,
                        dx: dX,
                        n: N): Array<Y> {
        val d = y0.size
        val r = ArrayUtils.twoDimArray(n to d)
        var t = x0

        (0 until d).forEach { i -> r[0][i] = y0[i] }

        for (i in 0 until n - 1) {
            r[i + 1] = r[i] + dx * f(t, r[i])
            t += dx
        }

        return r
    }

    override fun invoke(f: F, y: Y, x: X, dx: dX): Y {
        return y + dx * f(x, y)
    }
}