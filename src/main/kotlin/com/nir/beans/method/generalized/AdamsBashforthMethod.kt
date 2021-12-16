package com.nir.beans.method.generalized

import com.nir.beans.reaction.parsing.ExpressionParser
import com.nir.utils.math.F
import com.nir.beans.method.Method
import com.nir.utils.math.D
import com.nir.utils.math.N
import com.nir.utils.math.X
import com.nir.utils.math.Xs
import com.nir.utils.math.Y
import com.nir.utils.math.Ys
import com.nir.utils.math.dX
import com.nir.utils.times
import com.nir.utils.plus
import kotlin.properties.Delegates

class AdamsBashforthMethod internal constructor(
        name: String,
        val order: Int,
        betta: Array<Any>,
        val c: Any
)
    : Method("$name (Generalized)")
{

    private val bettas = betta.map { ExpressionParser.toDouble(it) }.toTypedArray()
    private val dXbettas by Delegates.notNull<Array<Double>>()
    private lateinit var Y : Array<Y>
    private val lastIndex = this.order - 1
    private var d by Delegates.notNull<Int>()

    fun setAccelerationPoints(accelerationPoints: Array<Y>) {
        if (accelerationPoints.size != 0) {
            throw RuntimeException("Acceleration points have to present")
        }
        if (accelerationPoints.size != this.order) {
            throw RuntimeException("Acceleration points amount should be the same as order of method")
        }
        this.Y = accelerationPoints
    }

    override fun setUp(dx: X, d: D): Method {
        this.d = d
        if (this.Y.isEmpty()) {
            throw RuntimeException("Acceleration points have to present")
        }
        return this
    }

    override fun invoke(f: F, x0: X, y0: Y, dx: dX, n: N): Pair<Xs, Ys> {
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun calc(x: X, ys: Array<Y>, f: F, dx: dX): Y {
        val ysLast = ys.size - 1
        val `Fn-1` = (0..ysLast).map { i ->
            val yi = ys[ysLast - i]
            f(x, yi)
        }.toTypedArray()

        val `Yn-1` = ys[ysLast]
        return `Yn-1` + this.dXbettas * `Fn-1`
    }
}