package com.nir.utils.math.solution

import com.nir.utils.math.X
import com.nir.utils.math.Y
import de.gsi.dataset.spi.DoubleDataSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class SolutionFlow(private val collectTo: List<DoubleDataSet>,
                   private val flow: Flow<Pair<X, Y>>) {

    suspend fun collect() {
        this.flow.collect { value ->
            collectTo.withIndex().forEach { (index, dataSet) ->
                val time = value.first
                val point = value.second[index]
                dataSet.add(time, point)
            }
        }
    }
}

class SolutionBatchFlow(private val dataSets: List<DoubleDataSet>,
                        private val flow: Flow<Pair<DoubleArray, Array<DoubleArray>>>) {

    suspend fun collect() {
        this.flow.collect { pairs ->
            dataSets.withIndex().forEach { (index, dataSet) ->
                val toDoubleArray = pairs.first
                val toDoubleArray2 = pairs.second[index]
                dataSet.add(toDoubleArray, toDoubleArray2)
            }
        }
    }
}
