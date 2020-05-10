package com.nir.utils.math.method.generalized

import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.method.ButchersTable
import com.nir.utils.math.method.F
import com.nir.utils.math.method.Method
import com.nir.utils.math.method.N
import com.nir.utils.math.method.X
import com.nir.utils.math.method.Xs
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.Ys
import com.nir.utils.math.method.dX
import com.nir.utils.math.method.k
import com.nir.utils.math.method.kFunc
import com.nir.utils.plus
import com.nir.utils.times

class GeneralizedExplicitRungeKuttaMethod
/**
 * @param s количество стадий.
 * @param p порядок метода.
 */
constructor(
        val s: Int,
        private val p: Int,
        private val butchersTable: ButchersTable,
        name: String
)
    : GeneralizedMethod(name)
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
