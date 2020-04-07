package com.nir.utils.math.solution

class Timer {
    private var start: Long = 0
    private var end: Long = 0
    private val allDurations = ArrayList<Long>()

    fun start(): Timer {
        this.start = System.currentTimeMillis()
        return this
    }

    fun end(): Long {
        this.end = System.currentTimeMillis()
        val duration = this.end - this.start
        allDurations.add(duration)
        return duration
    }

    fun whole(): Long {
        return allDurations.sum()
    }
}