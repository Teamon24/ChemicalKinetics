package com.nir.utils


typealias ResultAndCount<Result> = Pair<Result, Timer.CountInfo>

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
        val duration = get()
        allDurations.add(duration)
        return duration
    }

    fun get() = this.end - this.start

    fun total(): Long {
        return allDurations.sum()
    }

    data class CountInfo(val start: Long, val end: Long, val duration: Long)

    companion object {
        private const val formatMinSecMillis = "%02d:%02d:%03d"

        fun <Result> countMillis(action: () -> Result): ResultAndCount<Result> {
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
            return String.format(formatMinSecMillis, m, s, millis % 1000)
        }
    }
}