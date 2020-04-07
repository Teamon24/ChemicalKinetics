package com.nir.utils.math

import com.nir.beans.k
import com.nir.utils.ListUtils

typealias X = Double
typealias Y = Array<Double>

typealias KFunc = (system: System, x: X, y: Y) -> k

class ExplicitRungeKutta(
        val stages: Int,
        val order: Int,
        val butchersTable: ButchersTable,
        override val name: String) : Method() {


    var step: Double? = null
    var h: () -> Double = { step!! }
    private var K = ListUtils.arrayList(stages) { emptyKFunc }
    lateinit var core: (system: System, x: X, y: Y) -> Y

    init {
        initK()
        initCore()
    }

    private fun initK() {
        val c = butchersTable.c
        val A = butchersTable.A
        for (i in 0 until stages) {
            val k = getK(i, h, c, A, K)
            K[i] = k
        }
    }


    private fun initCore() {
        val b = butchersTable.b
        core = {
            system: System, x: X, y: Y
            ->
            h() * sum(stages, b, K)(system, x, y)
        }
    }

    override fun set(dimension: Int, h: Double) {
        this.step = h
    }

    override fun invoke(system: System, y0: Y, x0: X, h: X, N: Int): Array<Y> {
        val D = y0.size
        val y = ArrayUtils.twoDimArray(N to D)
        var x = h

        (0 until D).forEach { i -> y[0][i] = y0[i] }

        for (i in 0 until N - 1) {
            y[i + 1] = this(system, y[i], x, h)
            x += h
        }

        return y
    }

    override fun invoke(system: System, y: Y, x: X, h: X): Y {
        return y + core(system, x, y)
    }

    private fun getK(i: Int,
                     h: () -> Double,
                     c: Array<Double>,
                     A: Matrix<Double>,
                     Ks: ArrayList<KFunc>): (System, X, Y) -> Array<Double> {
        val Ai = A[i].toTypedArray()
        val sum = sum(i, Ai, Ks)
        return { system: System, x: X, y: Y
            ->
            system(x + c[i] * h(), y + h() * sum(system, x, y))
        }
                     }

    private fun sum(
            i: Int,
            Ai: Array<Double>,
            Ks: ArrayList<KFunc>
    )
            : KFunc
    {
        if (i == 0) return emptyKFunc

        val adds = Array(i) { emptyKFunc }
        for (j in 0 until i) {
            adds[j] = {
                system: System, x: X, y: Y
                ->
                Ai[j] * Ks[j](system, x, y)
            }
        }

        fun initial(y: Y) = Array(y.size) { 0.0 }

        return {
            system: System, x: X, y: Y
            ->
            adds.fold(initial(y)) { acc, add -> acc + add(system, x, y) }
        }
    }


}
