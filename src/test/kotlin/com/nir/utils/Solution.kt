package com.nir.utils

import com.nir.utils.math.F
import com.nir.utils.math.Method
import javafx.application.Platform
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Solution
constructor(
        private val initialData: InitialData,
        private val system: F,
        private val method: Method
) {
    fun start(
            titles: List<String>,
            primaryStage: Stage
    ) {
        val (t0, r0, dt, N) = initialData
        val datas = PlotUtils.show(titles, primaryStage)

        Platform.runLater() {
            CoroutineScope(Dispatchers.IO).launch {
                val series = PlotUtils.series(t0, N, dt)
                val solution = method(system, r0, t0, N, dt)
                datas.withIndex().forEach { (index, data) ->
                    data.add(series, solution.map { it[index] }.toDoubleArray())
                }
            }
        }
    }
}

