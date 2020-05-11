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

    data class CountInfo(val start: Long, val end: Long, val duration: Long)

    companion object {
        private const val defaultFormat = "%d:%02d:%02d:%03d"
        private const val format = "%dч. %02dм. %02dс. %03dмс."
        fun <Result> countMillis(action: () -> Result): Pair<Result, CountInfo> {
            val timer = Timer().start()
            val result = action()
            timer.stop()
            val countInfo = CountInfo(timer.start, timer.end, timer.total())
            return result to countInfo
        }

        fun formatMillis(millis: Long): String {
            val seconds = millis / 1000
            val s: Long = seconds % 60
            val m: Long = seconds / 60 % 60
            val h: Long = seconds / (60 * 60) % 24
            return String.format(defaultFormat, h, m, s, millis % 1000)
        }
    }
}