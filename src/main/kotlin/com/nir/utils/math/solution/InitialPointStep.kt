package com.nir.utils.math.solution

import com.nir.utils.math.InitialPoint
import com.nir.utils.math.X
import com.nir.utils.math.Y

class InitialPointStep(private val info: Solution.Info) {
    fun initialPoint(initialPoint: InitialPoint): DataSetsStep {
        this.info.initialPoint = initialPoint
        return DataSetsStep(this.info)
    }

    fun initialPoint(x0: X, y0: Y): DataSetsStep {
        this.info.initialPoint = InitialPoint(x0, y0)
        return DataSetsStep(this.info)
    }
}