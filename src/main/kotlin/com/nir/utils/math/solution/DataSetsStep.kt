package com.nir.utils.math.solution

import com.nir.beans.method.Method
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.F
import com.nir.utils.math.InitialPoint
import de.gsi.dataset.spi.DoubleDataSet

class DataSetsStep(
    private val method: Method,
    private val computationConfigs: ComputationConfigs,
    private val system: F,
    private val initialPoint: InitialPoint) {

    fun datasets(dataSets: List<DoubleDataSet>): TaskTypeStep {
        return TaskTypeStep(method, computationConfigs, system, initialPoint, dataSets)
    }
}