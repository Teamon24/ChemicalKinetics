package com.nir.utils.math.method

import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.InitialPoint

/**
 *
 */
val f = F(
        { _, y -> y[0] * y[0] + y[1] + y[2] },
        { _, y -> y[1] * y[1] + y[0] + y[2] },
        { _, y -> y[2] * y[2] + y[0] + y[1] }
)

val y0 = arrayOf(1.00, 1.00, 1.00)
val x0 = 5.0
val dx = 0.01
val initialPoint = InitialPoint(x0, y0)
val computationConfigs = ComputationConfigs(dx, 1)