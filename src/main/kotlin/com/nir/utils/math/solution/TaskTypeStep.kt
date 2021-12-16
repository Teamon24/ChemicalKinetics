package com.nir.utils.math.solution

import com.nir.beans.method.Method
import com.nir.utils.InitUtils
import com.nir.utils.Timer
import com.nir.utils.Timer.Companion.formatMillis
import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.D
import com.nir.utils.math.F
import com.nir.utils.math.InitialPoint
import de.gsi.dataset.spi.DoubleDataSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.FutureTask


class TaskTypeStep(
    private val method: Method,
    private val computationConfigs: ComputationConfigs,
    private val system: F,
    private val initialPoint: InitialPoint,
    private val dataSets: List<DoubleDataSet>
) {

    private val decimalFormat: DecimalFormat

    init{
        val nf = NumberFormat.getNumberInstance(Locale.US)
        nf.maximumFractionDigits = 2
        decimalFormat = nf as DecimalFormat
    }

    fun futureTask(): FutureTask<Long> {
        return FutureTask {
            val x0 = initialPoint.x0
            val y0 = initialPoint.y0
            val dx = computationConfigs.dx
            val n = computationConfigs.n

            println("${method.name}: started at ${formatMillis(System.currentTimeMillis())}.")

            val (solution, countInfo) = Timer.countMillis {
                method(system, x0, y0, dx, n)
            }

            println("${method.name}: calculation was ended. Duration: ${formatMillis(countInfo.duration)}")

            dataSets.withIndex().forEach { (index, dataSet) ->
                dataSet.add(solution.first, solution.second.map { it[index] }.toDoubleArray())
            }

            countInfo.duration
        }
    }

    /**
     * Подготовка объекта [SolutionFlowImpl] для вычислений решения системы.
     */
    fun flow(): SolutionFlowImpl {
        val flow = flow {
            val x0 = initialPoint.x0
            val y0 = initialPoint.y0
            val dx = computationConfigs.dx
            val n = computationConfigs.n
            val d = y0.size

            var x = x0
            val y = ArrayUtils.twoDimArray(2 to d)
            (0 until d).forEach { i -> y[0][i] = y0[i] }

            for (i in 0 until n - 1) {
                y[1] = method(system, x, y[0], dx)
                this.emit(x to y[1])
                x += dx
                y[0] = y[1]
                delay(1)
            }
            println("Flow ended its emissions")
        }
        return SolutionFlowImpl(DoubleDataSets(dataSets), flow)
    }

    /**
     * Подготовка объекта [Flow] для вычислений решения системы,
     * который будет выдавать данные порционально.
     * @param batchSize размер порции.
     */
    fun batchFlow(batchSize: Int): SolutionBatchFlow {
        val flow = flow {
            val x0 = initialPoint.x0
            val y0 = initialPoint.y0
            val dx = computationConfigs.dx
            val n = computationConfigs.n
            val d = y0.size

            var t = x0
            val r = ArrayUtils.twoDimArray(2 to d)
            (0 until d).forEach { i -> r[0][i] = y0[i] }

            val partT = initPartT(batchSize)
            val partR = initPartR(d, batchSize)
            var counter = 0
            var totalCounter = 0L
            var batchesCounter = 0

            val timer = Timer()
            timer.start()
            for (i in 0 until n - 1) {
                r[1] = method(system, t, r[0], dx)

                if (counter == batchSize) {
                    val duration = timer.stop()
                    val spentTime = timer.total()
                    batchesCounter++
                    println(message(batchesCounter, batchSize, duration, spentTime, n, totalCounter))
                    this.emit(partT to partR)
                    counter = 0
                    timer.start()
                } else {
                    partT[counter] = t
                    partR.withIndex().forEach {
                        it.value[counter] = r[1][it.index]
                    }
                    counter++
                    totalCounter++
                }
                t += dx
                r[0] = r[1]
            }

            if (partT.isNotEmpty()) {
                this.emit(partT to partR)
                println("The last batch with size '${partT.size}' was emitted")
            }
            println("Batch flow ended its emissions. Whole duration: ${timer.total()}")
        }
        return SolutionBatchFlow(DoubleDataSets(dataSets), flow)
    }

    fun batchFlow(): SolutionBatchFlow {
        val batchSize = computationConfigs.n/10
        return batchFlow(batchSize)
    }

    private fun message(batchesCounter: Int, batchSize: Int, duration: Long, totalTime: Long, n: Int, totalCounter: Long) =
                    """"${method.name}": batch #$batchesCounter with size '$batchSize' was emitted. Duration: ${formatMillis(duration)}. Total time: ${formatMillis(totalTime)}. Counted ${totalCounter.separate1000()} from ${n.separate1000()}."""

    private fun Int.separate1000() = decimalFormat.format(this)
    private fun Long.separate1000() = decimalFormat.format(this)

    private fun initPartR(d: D, batchSize: Int) = InitUtils.doubleArrays(d, batchSize)

    private fun initPartT(batchSize: Int) = DoubleArray(batchSize)

    private fun rToStr(r: Array<Double>) = r.withIndex().joinToString(separator = ", ", truncated = "") { (index, value) -> "r${index + 1} = $value" }

}