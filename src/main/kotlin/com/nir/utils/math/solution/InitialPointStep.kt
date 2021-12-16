package com.nir.utils.math.solution

import com.nir.utils.math.F
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.X
import com.nir.utils.math.Y

class InitialPointStep(private val system: F)
{
    fun initialPoint(initialPoint: InitialPoint): ComputationConfigStep {
        return ComputationConfigStep(system, initialPoint)
    }

    fun initialPoint(x0: X, y0: Y): ComputationConfigStep {
        return ComputationConfigStep(system, InitialPoint(x0, y0))
    }
}