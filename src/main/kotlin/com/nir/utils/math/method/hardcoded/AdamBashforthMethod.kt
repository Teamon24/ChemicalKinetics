package com.nir.utils.math.method.hardcoded

import com.nir.beans.Methods
import com.nir.utils.get
import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.method.D
import com.nir.utils.math.method.F
import com.nir.utils.math.method.Method
import com.nir.utils.math.method.N
import com.nir.utils.math.method.X
import com.nir.utils.math.method.Xs
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.Ys
import com.nir.utils.math.method.dX
import com.nir.utils.math.method.hardcoded.AdamBashforthMethods.getAccelerationPoints
import com.nir.utils.plus
import com.nir.utils.times
import kotlin.properties.Delegates

abstract class AdamBashforthMethod(
        val order: Int,
        private val coeffs: List<Double>) : HardcodedMethod("Adam-Bashforth Method of ${order}-order")
{
    private lateinit var firstAccelerationPointMethod: Method
    private lateinit var accelerationPoints : Array<Y>
    private var d by Delegates.notNull<D>()

    override fun setUp(initialPoint: InitialPoint, computationConfig: ComputationConfigs): Method {
        this.d = initialPoint.y0.size
        return this
    }

    fun setFirstAccelerationPointMethodName(firstAccelerationPointMethodName: String) {
        this.firstAccelerationPointMethod = Methods.getByName(firstAccelerationPointMethodName)
    }

    fun setFirstAccelerationPointMethod(firstAccelerationPointMethod: Method) {
        this.firstAccelerationPointMethod = firstAccelerationPointMethod
    }

    override fun invoke(f: F, x0: X, y0: Y, dx: dX, n: N): Pair<Xs, Ys> {

        this.accelerationPoints = getAccelerationPoints(
                order,
                firstAccelerationPointMethod,
                f,
                InitialPoint(x0, y0),
                ComputationConfigs(dx, n)
        )

        val coeffs2 = dx * coeffs
        val d = y0.size
        val shift = this.accelerationPoints.size + 1
        val ys = y0 + this.accelerationPoints + ArrayUtils.twoDimArray(n - shift to d)
        val xs = initXs(n, x0)

        for (j in this.accelerationPoints.indices) {
            xs[j+1] = xs[j]+dx
        }

        var x = xs[shift - 1]

        for (i in 0 until n - shift) {
            x += dx
            xs[i + shift] = x
            val get = ys.get(i, amount = order)
            val fs = (get.map { f(x, it) })
            val core = coeffs2 * fs
            ys[i + shift] = ys[i + shift - 1] + core
        }
        return xs to ys
    }

    override fun invoke(f: F, x: X, y: Y, dx: dX): Y {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

private operator fun Y.plus(arrays: Array<Y>): Array<Y> {
    val result = Array(1 + arrays.size) { this.empty() }
    result[0] = this
    for (i in arrays.indices) {
        result[i+1] = arrays[i]
    }
    return result
}

private fun Y.empty(): Y {
    return Array(this.size) { 0.0 }
}

