package com.nir.beans.method.hardcoded

import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.D
import com.nir.utils.math.F
import com.nir.utils.math.N
import com.nir.utils.math.X
import com.nir.utils.math.Y
import com.nir.utils.math.dX

interface RungeKuttaCore {
    fun setUp(d: D, dx: dX)

    operator fun invoke(f: F, y: Y, x: X, dx: dX): Array<Double>
}

fun init(n: N, d: D) = ArrayUtils.twoDimArray(n to d)



