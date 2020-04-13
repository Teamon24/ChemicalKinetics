package com.nir.utils.math

import com.nir.utils.math.method.N
import com.nir.utils.math.method.X
import com.nir.utils.math.method.Y
import com.nir.utils.math.method.dX

data class InitialPoint(val x0: X, val y0: Y)

data class ComputationConfigs(
        val dx: dX,
        val n: N
)