package com.nir.utils.math.solution

import com.nir.utils.math.InitialData
import com.nir.utils.math.method.X
import com.nir.utils.math.method.Y

class InitialDataStep(private val info: Solution.Info) {
    fun initialData(initialData: InitialData): DataSetsStep {
        this.info.initialData = initialData
        return DataSetsStep(this.info)
    }

    fun initialData(x0: X, y0: Y): DataSetsStep {
        this.info.initialData = InitialData(x0, y0)
        return DataSetsStep(this.info)
    }
}