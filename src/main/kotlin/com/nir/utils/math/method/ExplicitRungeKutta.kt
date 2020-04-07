package com.nir.utils.math.method

import com.nir.beans.k
import com.nir.utils.InitUtils
import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.Matrix
import com.nir.utils.math.plus
import com.nir.utils.math.times



typealias kFunc = (f: F, x: X, y: Y) -> k

typealias k = Array<Double>

class ExplicitRungeKutta(
        val stages: Int,
        val order: Int,
        val butchersTable: ButchersTable,
        override val name: String) : Method() {


    var step: Double? = null
    var dx: () -> Double = { step!! }
    private var K = InitUtils.arrayList(stages) { emptyKFunc }
    lateinit var core: (f: F, x: X, y: Y) -> Y

    init {
        initK()
        initCore()
    }

    private fun initK() {
        val c = butchersTable.c
        val A = butchersTable.A
        for (i in 0 until stages) {
            val k = getK(i, dx, c, A, K)
            K[i] = k
        }
    }


    private fun initCore() {
        val b = butchersTable.b
        core = {
            f: F, x: X, y: Y
            ->
            dx() * sum(stages, b, K)(f, x, y)
        }
    }

    override fun set(d: D, dx: dX) {
        this.step = dx
    }

    override fun invoke(f: F, y0: Y, x0: X, dx: dX, n: Int): Array<Y> {
        val d = y0.size
        val y = ArrayUtils.twoDimArray(n to d)
        var x = dx

        (0 until d).forEach { i -> y[0][i] = y0[i] }

        for (i in 0 until n - 1) {
            y[i + 1] = this(f, y[i], x, dx)
            x += dx
        }

        return y
    }

    override fun invoke(f: F, y: Y, x: X, dx: dX): Y {
        return y + core(f, x, y)
    }

    private fun getK(i: Int,
                     dx: () -> Double,
                     c: Array<Double>,
                     A: Matrix<Double>,
                     Ks: ArrayList<kFunc>): (F, X, Y) -> k
    {
        val Ai = A[i].toTypedArray()
        val sum = sum(i, Ai, Ks)
        return { f: F, x: X, y: Y
            ->
            f(x + c[i] * dx(), y + dx() * sum(f, x, y))
        }
                     }

    private fun sum(
            i: Int,
            Ai: Array<Double>,
            Ks: ArrayList<kFunc>
    )
            : kFunc
    {
        if (i == 0) return emptyKFunc

        val adds = Array(i) { emptyKFunc }
        for (j in 0 until i) {
            adds[j] = {
                f: F, x: X, y: Y
                ->
                Ai[j] * Ks[j](f, x, y)
            }
        }

        fun initial(y: Y) = Array(y.size) { 0.0 }

        return {
            f: F, x: X, y: Y
            ->
            adds.fold(initial(y)) { acc, add -> acc + add(f, x, y) }
        }
    }


}
