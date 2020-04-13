package com.nir.utils.math.solution

import com.nir.utils.math.InitialPoint
import com.nir.utils.math.method.X
import com.nir.utils.math.method.Y

class InitialDataStep(private val info: Solution.Info) {
    fun initialData(initialPoint: InitialPoint): DataSetsStep {
        this.info.initialPoint = initialPoint
        return DataSetsStep(this.info)
    }

    fun initialData(x0: X, y0: Y): DataSetsStep {
        this.info.initialPoint = InitialPoint(x0, y0)
        return DataSetsStep(this.info)
    }
}