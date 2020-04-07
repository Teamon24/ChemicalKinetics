package com.nir.utils.math.solution

import com.nir.utils.math.InitialData

class InitialDataStep(private val info: Solution.Info) {

    fun initialData(initialData: InitialData): DataSetsStep {
        this.info.initialData = initialData
        return DataSetsStep(this.info)
    }
}