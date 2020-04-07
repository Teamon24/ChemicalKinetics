package com.nir.utils.math.solution

import de.gsi.dataset.spi.DoubleDataSet

class DataSetsStep(private val info: Solution.Info) {

    fun datasets(dataSets: List<DoubleDataSet>): TypeStep {
        this.info.dataSets = dataSets
        return TypeStep(this.info)
    }
}