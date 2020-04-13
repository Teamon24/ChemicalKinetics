package com.nir.utils.math.method

import com.nir.beans.ExpressionParser
import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.times
import com.nir.utils.math.plus
import kotlin.properties.Delegates

class AdamsBashforthMethod internal constructor(
        override val name: String,
        val order: Int,
        betta: Array<Any>,
        val c: Any
) : Method() {

    private val bettas = betta.map { ExpressionParser.toDouble(it) }.toTypedArray()
    private lateinit var Y : Array<Y>
    private lateinit var X : Array<X>
    var counter = 0
    private val lastIndex = this.order - 1
    private var d by Delegates.notNull<Int>()

    override fun init(
            initialPoint: InitialPoint,
            computationConfig: ComputationConfigs
    ): Method {
        this.d = initialPoint.y0.size
        this.Y = zeros(this.order, d)
        this.X = zeros(this.order)
        return this
    }

    override fun invoke(f: F, x0: X, y0: Y, dx: dX, n: N): Array<Y> {
        val d = y0.size
        val y = ArrayUtils.twoDimArray(n to d)
        var x = x0

        (0 until d).forEach { i -> y[0][i] = y0[i] }

        for (i in 0 until n - 1) {
            y[i + 1] = this(f, x, y[i], dx)
            x += dx
        }

        return y
    }

    override fun invoke(f: F, x: X, y: Y, dx: dX): Y {

        X[0] = x //x0
        Y[0] = y //y0

        //расчет предыдущих значений x
        (1..lastIndex).forEach { i ->
            this.X[i] = x + dx * i
        }

        //расчет предыдущих значений y
        (1..lastIndex).forEach { i ->
            val xs = this.X.dropLast(this.order-i).toTypedArray()
            val ys = this.Y.dropLast(this.order-i).toTypedArray()
            Y[i] = AdamsBashforthMethods[i].calc(xs, ys, f, dx)
        }

        val lastY = Yn(f, dx)
        return lastY
    }

    fun calc(xs: Array<X>, ys: Array<Y>, f: F, dx: dX): Y {
        val xsLast = xs.size - 1
        val ysLast = ys.size - 1
        val `Fn-1` = (0..ysLast).map { i ->
            val xi = xs[xsLast - i]
            val yi = ys[ysLast - i]
            f(xi, yi)
        }.toTypedArray()

        val `Yn-1` = ys[ysLast]
        return `Yn-1` + dx * (bettas * `Fn-1`)
    }

    private fun Yn(f: F, dx: dX): Array<Double> {
        val `Fn-1` = (0 until this.order).map { i ->
            val xi = this.X[lastIndex - i]
            val yi = this.Y[lastIndex - i]
            f(xi, yi)
        }.toTypedArray()

        val `Yn-1` = Y[lastIndex]
        val Yn = `Yn-1` + dx * (bettas * `Fn-1`)
        return Yn
    }

    /**
     * x0, x1, x2, ... xk -> x1, x2, ..., xk, 0
     * Y0, Y1, Y2, ... Yk -> Y1, Y2, ..., Yk, 0
     */
    private fun shiftLeftPrevs() {
        (0 until lastIndex).forEach { i ->
            this.X[i] = this.X[i+1]
            this.Y[i] = this.Y[i+1]
        }
        this.X[lastIndex] = 0.0
        this.Y[lastIndex] = zeros(d)
    }
}