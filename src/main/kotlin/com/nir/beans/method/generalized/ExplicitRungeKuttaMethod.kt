package com.nir.beans.method.generalized

import com.nir.beans.method.ButchersTable
import com.nir.utils.math.F
import com.nir.beans.method.Method
import com.nir.utils.math.D
import com.nir.utils.math.N
import com.nir.utils.math.X
import com.nir.utils.math.Xs
import com.nir.utils.math.Y
import com.nir.utils.math.Ys
import com.nir.utils.math.dX
import com.nir.utils.math.k
import com.nir.utils.math.kFunc
import com.nir.utils.plus
import com.nir.utils.times

class ExplicitRungeKuttaMethod
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
    : Method("$name (Generalized)")
{

    private val emptyKFunc = { _: F, _: X, y: Y -> Array(y.size) { 0.0 } }

    private var d: Int = 1
    private var dx: Double = 0.0
    private var K = Array(s) { emptyKFunc }
    private lateinit var core: (f: F, x: X, y: Y, dx: dX) -> Y

    override fun setUp(dx: X, d: D): Method {
        this.dx = dx
        this.d = d
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
                K[i] = { f, x, y ->
                    f(
                        x + c[i] * dx,
                        y + dx * sumOfProd(i, A[i], K)(f, x, y)
                    )
                }
            }
        }
    }

    private fun initCore() {
        val b = butchersTable.b
        core = { f: F, x: X, y: Y, dx: dX -> y + dx * sumOfProd(s, b, K)(f, x, y) }
    }

    private fun sumOfProd(i: Int, values: Array<Double>, kFuncs: Array<kFunc>): kFunc
    {
        val count = values.count { it != 0.0 }
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
