package com.nir.utils.math.method.hardcoded

import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.method.D
import com.nir.utils.math.method.F
import com.nir.utils.math.method.N
import com.nir.utils.math.method.X
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.dX

interface RungeKuttaCore {
    fun setUp(d: D, dx: dX)

    operator fun invoke(f: F, y: Y, x: X, dx: dX): Array<Double>
}

fun init(n: N, d: D) = ArrayUtils.twoDimArray(n to d)



