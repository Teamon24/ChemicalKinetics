package com.nir.utils

import com.nir.utils.math.solution.SolutionFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

object PlatformUtils {

    @JvmStatic
    fun <T> runLater(solutionFlow: SolutionFlow<T>, onComplete: () -> Unit = {}) {
        Timer.countMillis {
            CoroutineScope(Dispatchers.IO).async {
                solutionFlow.collect()
                onComplete()
            }
        } count {
            println("Start: $start, end: $end. Duration: $duration")
        }
    }
}

private infix fun ResultAndCount<*>.count(function: Timer.CountInfo.() -> Unit): ResultAndCount<*> {
    this.second.function()
    return this
}
