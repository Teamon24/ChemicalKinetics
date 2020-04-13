package com.nir.utils.math.method

import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.Matrix2
import com.nir.utils.math.plus
import com.nir.utils.math.times

class ExplicitRungeKuttaMethod(
        val stages: Int,
        val order: Int,
        val butchersTable: ButchersTable,
        override val name: String
) : Method()
{

    private val emptyKFunc = { _: F, _: X, y: Y -> Array(y.size) { 0.0 } }

    private var dx: Double = 0.0
    private var K = Array(stages) { emptyKFunc }
    private lateinit var core: (f: F, x: X, y: Y) -> Y

    override fun init(
            initialPoint: InitialPoint,
            computationConfig: ComputationConfigs
    ): Method {
        this.dx = computationConfig.dx
        initK()
        initCore()
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
        return core(f, x, y)
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
        val sumOfProd = sumOfProd(stages, b, K)
        core = { f: F, x: X, y: Y -> y + dx * sumOfProd(f, x, y) }
    }

    private fun getK(i: Int,
                     dx: Double,
                     c: Array<Double>,
                     A: Matrix2<Double>,
                     Ks: Array<kFunc>): (F, X, Y) -> k
    {
        val Ai = A[i]
        val sumOfProd = sumOfProd(i, Ai, Ks)
        val k: (f:F, x:X, y:Y) -> k = { f, x, y -> f(x + c[i] * dx, y + dx * sumOfProd(f, x, y)) }
        return k
    }

    private fun sumOfProd(i: Int, values: Array<Double>, kFuncs: Array<kFunc>): kFunc
    {
        if (i == 0) return emptyKFunc

        val adds = Array(i) { emptyKFunc }
        for (j in 0 until i) {
            if (values[j] != 0.0) {
                adds[j] = { f: F, x: X, y: Y -> values[j] * kFuncs[j](f, x, y) }
            }
        }

        return { f: F, x: X, y: Y -> adds.fold(initial(y)) { acc, add -> acc + add(f, x, y) } }
    }

    private fun initial(y: Y) = Array(y.size) { 0.0 }
}
