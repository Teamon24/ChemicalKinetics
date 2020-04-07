package com.nir.utils

import com.nir.utils.math.R
import com.nir.utils.math.T
import de.gsi.dataset.spi.DoubleDataSet
import javafx.application.Platform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SolutionFlow(private val collectTo: List<DoubleDataSet>,
                   private val flow: Flow<Pair<T, R>>) {

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

class SolutionBatchFlow(private val collectTo: List<DoubleDataSet>,
                        private val flow: Flow<Pair<ArrayList<T>, ArrayList<ArrayList<Double>>>>) {

    suspend fun collect() {
        this.flow.collect { pairs ->
            collectTo.withIndex().forEach { (index, dataSet) ->
                val toDoubleArray = pairs.first.toDoubleArray()
                val toDoubleArray2 = pairs.second[index].toDoubleArray()
                dataSet.add(toDoubleArray, toDoubleArray2)
            }
        }
    }
}

object PlatformUtils {

    @JvmStatic
    fun runLater(flowSolutionBatchFlow: SolutionBatchFlow) {
        Platform.runLater() {
            CoroutineScope(Dispatchers.IO).launch {
                flowSolutionBatchFlow.collect()
            }
        }
    }

    @JvmStatic
    fun runLater(flow: SolutionFlow) {
        Platform.runLater() {
            CoroutineScope(Dispatchers.IO).launch {
                flow.collect()
            }
        }
    }

    @JvmStatic
    fun runLater(runnable: Runnable) {
        Platform.runLater(runnable)
    }


}