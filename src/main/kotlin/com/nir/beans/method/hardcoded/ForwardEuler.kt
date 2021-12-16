package com.nir.beans.method.hardcoded

import com.nir.beans.method.Method
import com.nir.utils.math.D
import com.nir.utils.math.F
import com.nir.utils.math.N
import com.nir.utils.math.X
import com.nir.utils.math.Xs
import com.nir.utils.math.Y
import com.nir.utils.math.Ys
import com.nir.utils.math.dX
import com.nir.utils.plus
import com.nir.utils.times

object ForwardEuler : HardcodedMethod("Forward Euler") {
    override fun setUp(dx: X, d: D): Method {
        return this
    }

    override fun invoke(f: F,
                        x0: X,
                        y0: Y,
                        dx: dX,
                        n: N
    ): Pair<Xs, Ys>
    {
        val ys = initYs(n, y0)
        val xs = initXs(n, x0)
        var x = x0

        for (i in 0 until n - 1) {
            x += dx
            xs[i + 1] = x
            ys[i + 1] = this(f, x, ys[i], dx)
        }

        return xs to ys
    }

    override fun invoke(f: F, x: X, y: Y, dx: dX): Y {
        return y + dx * f(x, y)
    }
}