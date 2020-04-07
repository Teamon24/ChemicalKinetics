package com.nir.utils

import com.nir.utils.math.solution.SolutionBatchFlow
import com.nir.utils.math.solution.SolutionFlow
import javafx.application.Platform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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