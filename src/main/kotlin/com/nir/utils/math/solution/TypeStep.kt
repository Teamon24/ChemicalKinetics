package com.nir.utils.math.solution

import com.nir.utils.InitUtils
import com.nir.utils.PlotUtils
import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.method.D

class TypeStep(private val info: Solution.Info) {


    fun task(): Runnable {
        return Runnable {
            val (method, system, dataSets, y0, x0, dx, n, _) = info.datas()
            var x = x0
            val series = PlotUtils.series(x0, n, dx)
            val timer = Timer().start()
            val solution = method(system, y0, x0, dx, n)
            timer.end()
            println("Calculation was ended in runnable task. Duration: ${timer.whole()} ")
            dataSets.withIndex().forEach { (index, dataSet) ->
                dataSet.add(series, solution.map { it[index] }.toDoubleArray())
            }
        }
    }

    fun flow(): SolutionFlow {
        val flow = kotlinx.coroutines.flow.flow {
            val (method, system, _, y0, x0, dx, n, d) = info.datas()
            var x = x0
            val y = ArrayUtils.twoDimArray(2 to d)
            (0 until d).forEach { i -> y[0][i] = y0[i] }

            for (i in 0 until n - 1) {
                y[1] = method(system, y[0], x, dx)
                this.emit(x to y[1])
                x += dx
                y[0] = y[1]
                kotlinx.coroutines.delay(1)
            }
            println("Flow ended its emissions")
        }
        return SolutionFlow(this.info.dataSets, flow)
    }

    fun batchFlow(): SolutionBatchFlow {
        val batchSize = this.info.initialData.n / 10
        return batchFlow(batchSize)
    }

    fun batchFlow(batchSize: Int): SolutionBatchFlow {
        val flow = kotlinx.coroutines.flow.flow {
            val (method, system, _, y0, x0, dx, n, d) = info.datas()
            var t = x0
            val r = ArrayUtils.twoDimArray(2 to d)
            (0 until d).forEach { i -> r[0][i] = y0[i] }

            val partT = initPartT(batchSize)
            val partR = initPartR(d, batchSize)
            var counter = 0;
            var batchesCounter = 0;

            val timer = Timer()
            timer.start()
            for (i in 0 until n - 1) {
                r[1] = method(system, r[0], t, dx)

                if (counter == batchSize) {
                    val duration = timer.end()
                    batchesCounter++
                    println(""""${info.method.name}": batch #$batchesCounter with size '$batchSize' was emitted. Duration: $duration ms""")
                    this.emit(partT to partR)
                    counter = 0
                    oneDelay()
                    timer.start()
                } else {
                    partT[counter] = t
                    partR.withIndex().forEach {
                        it.value[counter] = r[1][it.index]
                    }
                    counter++
                }
                t += dx
                r[0] = r[1]
            }

            if (partT.isNotEmpty()) {
                this.emit(partT to partR)
                println("The last batch with size '$counter' was emitted")
            }
            println("Batch flow ended its emissions. Whole duration: ${timer.whole()}")
        }
        return SolutionBatchFlow(this.info.dataSets, flow)
    }

    private fun initPartR(d: D, batchSize: Int) = InitUtils.doubleArrays(d, batchSize)

    private fun initPartT(batchSize: Int) = DoubleArray(batchSize)

    private suspend fun delay(batchSize: Int) {
        val timeMillis = batchSize / 3_000L
        println("Delay for millis: $timeMillis")
        kotlinx.coroutines.delay(timeMillis)
    }

    private suspend fun oneDelay() {
        kotlinx.coroutines.delay(1)
    }

    private fun rToStr(r: Array<Double>) = r.withIndex().joinToString(separator = ", ", truncated = "") { (index, value) -> "r${index + 1} = $value" }

}