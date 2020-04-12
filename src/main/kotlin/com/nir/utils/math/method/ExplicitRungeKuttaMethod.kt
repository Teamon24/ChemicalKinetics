package com.nir.utils.math.method

import com.nir.beans.k
import com.nir.utils.InitUtils
import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialData
import com.nir.utils.math.Matrix
import com.nir.utils.math.plus
import com.nir.utils.math.times
import java.lang.IllegalStateException

class ExplicitRungeKuttaMethod(
        val stages: Int,
        val order: Int,
        val butchersTable: ButchersTable,
        override val name: String
) : Method()
{

    private val emptyKFunc = { f: F, x: X, y: Y -> Array(y.size) { 0.0 } }

    var dx: Double? = null
    var getDx: () -> Double = {
        if(dx == null)
            throw IllegalStateException("You should set dx to Method before computations by invoking init method.")
        else dx!!
    }

    private var K = InitUtils.arrayList(stages) { emptyKFunc }
    lateinit var core: (f: F, x: X, y: Y) -> Y

    init {
        initK()
        initCore()
    }

    override fun init(
            initialData: InitialData,
            computationConfig: ComputationConfigs
    ): Method {
        this.dx = computationConfig.dx
        return this
    }

    override fun invoke(f: F, y0: Y, x0: X, dx: dX, n: Int): Array<Y> {
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
        return y + core(f, x, y)
    }

    private fun initK() {
        val c = butchersTable.c
        val A = butchersTable.A
        for (i in 0 until stages) {
            val k = getK(i, getDx, c, A, K)
            K[i] = k
        }
    }

    private fun initCore() {
        val b = butchersTable.b
        core = {
            f: F, x: X, y: Y
            ->
            getDx() * sumOfProd(stages, b, K)(f, x, y)
        }
    }

    private fun getK(i: Int,
                     dx: () -> Double,
                     c: Array<Double>,
                     A: Matrix<Double>,
                     Ks: ArrayList<kFunc>): (F, X, Y) -> k
    {
        val Ai = A[i].toTypedArray()
        val sum = sumOfProd(i, Ai, Ks)
        return { f: F, x: X, y: Y
            ->
            f(x + c[i] * dx(), y + dx() * sum(f, x, y))
        }
    }

    private fun sumOfProd(i: Int, first: Array<Double>, second: ArrayList<kFunc>): kFunc
    {
        if (i == 0) return emptyKFunc

        val adds = Array(i) { emptyKFunc }
        for (j in 0 until i) {
            if (first[j] == 0.0) {
                adds[j] = emptyKFunc
            } else {
                adds[j] = {
                    f: F, x: X, y: Y
                    ->
                    first[j] * second[j](f, x, y)
                }
            }

        }

        return {
            f: F, x: X, y: Y
            ->
            adds.fold(initial(y)) { acc, add -> acc + add(f, x, y) }
        }
    }

    private fun initial(y: Y) = Array(y.size) { 0.0 }
}
