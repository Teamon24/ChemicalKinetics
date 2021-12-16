package com.nir.utils

import com.nir.utils.math.solution.SolutionFlow
import javafx.application.Platform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object PlatformUtils {

    @JvmStatic
    fun runLater(flow: SolutionFlow) {
        Timer.countMillis {
            CoroutineScope(Dispatchers.IO).launch {
                flow.collect()
            }
        } count {
            println("Start: $start, end: $end. Duration: $duration")
        }
    }

    @JvmStatic
    fun runLater(runnable: Runnable) {
        Platform.runLater(runnable)
    }
}

private infix fun ResultAndCount<*>.count(function: Timer.CountInfo.() -> Unit): ResultAndCount<*> {
    this.second.function()
    return this
}
