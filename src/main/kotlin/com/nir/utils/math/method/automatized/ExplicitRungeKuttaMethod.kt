package com.nir.utils.math.method.automatized

import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.method.ButchersTable
import com.nir.utils.math.method.F
import com.nir.utils.math.method.N
import com.nir.utils.math.method.X
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.dX
import com.nir.utils.math.method.k
import com.nir.utils.math.method.kFunc
import com.nir.utils.math.plus
import com.nir.utils.math.times

class ExplicitRungeKuttaMethod
/**
 * @param s количество стадий.
 * @param p порядок метода.
 */
constructor(
        val s: Int,
        private val p: Int,
        private val butchersTable: ButchersTable,
        override val name: String
) : Method()
{

    private val emptyKFunc = { _: F, _: X, y: Y -> Array(y.size) { 0.0 } }

    private var d: Int = 1
    private var dx: Double = 0.0
    private var K = Array(s) { emptyKFunc }
    private lateinit var core: (f: F, x: X, y: Y, dx: dX) -> Y

    override fun setUp(
            initialPoint: InitialPoint,
            computationConfig: ComputationConfigs
    ): Method {
        this.dx = computationConfig.dx
        this.d = initialPoint.y0.size
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
        return core(f, x, y, dx)
    }

    private fun initK() {
        val c = butchersTable.c
        val A = butchersTable.A
        for (i in 0 until s) {
            if (i == 0) {
                K[i] = { f, x, y -> f(x, y) }
            } else {
                K[i] = { f, x, y -> f(x + c[i] * dx, y + dx * sumOfProd(i, A[i], K)(f, x, y)) }
            }
        }
    }

    private fun initCore() {
        val b = butchersTable.b
        core = { f: F, x: X, y: Y, dx: dX -> y + dx * sumOfProd(s, b, K)(f, x, y) }
    }

    private fun sumOfProd(i: Int, values: Array<Double>, kFuncs: Array<kFunc>): kFunc
    {
        val count = values.filter { it != 0.0 }.count()
        val adds = ArrayList<(f: F, x: X, y: Y) -> k>(count)
        for (j in 0 until i) {
            if (values[j] != 0.0) {
                adds.add { f: F, x: X, y: Y -> values[j] * kFuncs[j](f, x, y) }
            }
        }
        val zeros = Array(d) { 0.0 }
        return { f: F, x: X, y: Y -> adds.fold(zeros) { acc, add -> acc + add(f, x, y) } }
    }
}
