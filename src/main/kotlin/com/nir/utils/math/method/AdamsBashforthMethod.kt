package com.nir.utils.math.method

import com.nir.beans.ExpressionParser
import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialData
import com.nir.utils.math.times
import com.nir.utils.math.plus
import kotlin.properties.Delegates

class AdamsBashforthMethod(
        override val name: String,
        val order: Int,
        betta: Array<Any>,
        val c: Any
) : Method() {

    private val bettas = betta.map { ExpressionParser.toDouble(it) }.toTypedArray()
    var firstInvoke = true
    private lateinit var prevYs : Array<Y>
    private lateinit var prevXs : Array<X>
    private val lastIndex = this.order - 1
    private var d by Delegates.notNull<Int>()

    override fun init(
            initialData: InitialData,
            computationConfig: ComputationConfigs
    ): Method {
        this.d = initialData.y0.size
        this.prevYs = zeros(this.order, d)
        this.prevXs = zeros(this.order)
        return this
    }

    override fun invoke(f: F, y0: Y, x0: X, dx: dX, n: N): Array<Y> {
        val d = y0.size
        val y = ArrayUtils.twoDimArray(n to d)
        var x = x0

        (0 until d).forEach { i -> y[0][i] = y0[i] }

        for (i in 0 until n - 1) {
            y[i + 1] = this(f, y[i], x, dx)
            x += dx
        }

        return y
    }

    override fun invoke(f: F, y: Y, x: X, dx: dX): Y {
        if (firstInvoke) {

            prevXs[0] = x //x0
            prevYs[0] = y //y0

            //расчет предыдущих значений x
            (1..lastIndex).forEach { i ->
                this.prevXs[i] = x + dx * i
            }

            //расчет предыдущих значений y
            (1..lastIndex).forEach { i ->
                val method = AdamsBashforthMethods[i]
                prevYs[i] = method(f, prevYs[0], prevXs[0], dx)
            }
            firstInvoke = false
        }

        val prevF = (0 until this.order).map { i ->
            val xi = this.prevXs[lastIndex - i]
            val yi = this.prevYs[lastIndex - i]
            f(xi, yi)
        }.toTypedArray()

        val lastY = prevYs[lastIndex] + dx * (bettas * prevF)
        return lastY
    }

    /**
     * x0, x1, x2, ... xk -> x1, x2, ..., xk, 0
     * Y0, Y1, Y2, ... Yk -> Y1, Y2, ..., Yk, 0
     */
    private fun shiftLeftPrevs() {
        (0 until lastIndex).forEach { i ->
            this.prevXs[i] = this.prevXs[i+1]
            this.prevYs[i] = this.prevYs[i+1]
        }
        this.prevXs[lastIndex] = 0.0
        this.prevYs[lastIndex] = zeros(d)
    }
}