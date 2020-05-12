package com.nir

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

object PlatformUtils {

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