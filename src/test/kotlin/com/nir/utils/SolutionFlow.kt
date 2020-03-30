package com.nir.utils

import com.nir.utils.math.F
import com.nir.utils.math.flow.MethodWithFlow
import javafx.application.Platform
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SolutionFlow(private val initialData: InitialData,
                   private val system: F,
                   val methodWithFlow: MethodWithFlow) {
    fun start(
            titles: List<String>,
            primaryStage: Stage
    ) {
        val (t0, r0, dt, N) = initialData
        val titlesAndDatas = PlotUtils.show(titles, primaryStage)

        Platform.runLater() {
            val solutionFlow = methodWithFlow(system, r0, t0, N, dt)
            CoroutineScope(Dispatchers.IO).launch {
                solutionFlow.collect { value ->
                    titlesAndDatas.withIndex().forEach {
                        (index, dataSet) ->
                        val points = value.first[index]
                        val timeSeries = value.second
                        dataSet.add(timeSeries, points)
                    }
                }
            }
        }
    }
}

