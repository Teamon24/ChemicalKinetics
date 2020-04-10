package com.nir.utils


class Timer {
    private var start: Long = 0
    private var end: Long = 0
    private val allDurations = ArrayList<Long>()

    fun start(): Timer {
        this.start = System.currentTimeMillis()
        return this
    }

    fun stop(): Long {
        this.end = System.currentTimeMillis()
        val duration = this.end - this.start
        allDurations.add(duration)
        return duration
    }

    fun total(): Long {
        return allDurations.sum()
    }

    companion object {
        fun <Result> measureMillis(action: () -> Result): Pair<Result, Long> {
            val timer = Timer().start()
            val result = action()
            timer.stop()
            return result to timer.total()
        }

        fun formatMillis(millis: Long): String {
            val seconds = millis / 1000
            val s: Long = seconds % 60
            val m: Long = seconds / 60 % 60
            val h: Long = seconds / (60 * 60) % 24
            return String.format("%d:%02d:%02d:%03d", h, m, s, millis % 1000)
        }
    }
}