package com.nir.utils.math.solution

import com.nir.beans.method.Method
import com.nir.utils.InitUtils
import com.nir.utils.MessageUtils
import com.nir.utils.Timer
import com.nir.utils.Timer.Companion.formatMillis
import com.nir.utils.math.ArrayUtils
import com.nir.utils.math.ComputationConfigs
import com.nir.utils.math.D
import com.nir.utils.math.F
import com.nir.utils.math.InitialPoint
import com.nir.utils.math.N
import com.nir.utils.math.X
import com.nir.utils.math.XY
import com.nir.utils.math.Y
import com.nir.utils.math.dX
import de.gsi.dataset.spi.DoubleDataSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.transform
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask


class FlowTypeStep(
    private val method: Method,
    private val computationConfigs: ComputationConfigs,
    private val system: F,
    private val initialPoint: InitialPoint,
    private val dataSets: List<DoubleDataSet>
) {

    fun futureTask(): FutureTask<Long> {
        return FutureTask {
            val (x0, y0) = getInitialPoint()
            val (dx, n) = getStepAndSteps()

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
        val (x0, y0) = getInitialPoint()
        val (dx, n) = getStepAndSteps()
        val d = y0.size
        var x = x0
        val y = initY(d, y0)

        val flow = flow {
            for (i in 0 until n - 1) {
                y[1] = method(system, x, y[0], dx)
                this.emit(x to y[1])
                println(MessageUtils.emitionMessage(method, n, i))
                x += dx
                y[0] = y[1]
                delay(1)
            }
            println("Flow ended its emissions")
        }
        return SolutionFlowImpl(this.method.name, DoubleDataSets(dataSets), flow)
    }

    fun listFlow(batchSize: Int): SolutionListFlow {
        val container = MutableList(batchSize) { (0.0 to 0.0) as XY }
        var counter = 0
        val timer = Timer()
        val flow = flow()
            .getFlow()
            .transform {
                if (counter < batchSize) {
                    timer.start()
                    container[counter] = it
                    counter++
                } else {
                    timer.stop()
                    emit(container)
                    println("ListFlow: emition${container}")
                    counter = 0
                }
            }
            .onCompletion {
                if (counter != 0) {
                    val lastAmount = batchSize - counter
                    val value = container.take(lastAmount).toMutableList()
                    emit(value)
                }
            }

        return SolutionListFlow(this.method.name, DoubleDataSets(dataSets), flow)
    }


    /**
     * Подготовка объекта [Flow] для вычислений решения системы,
     * который будет выдавать данные порционально.
     * @param batchSize размер порции.
     */
    fun batchFlow(batchSize: Int): SolutionBatchFlow {
        val (x0, y0) = getInitialPoint()
        val (dx, n) = getStepAndSteps()
        val d = y0.size
        var x = x0
        val y = initY(d, y0)
        val partT = initPartT(batchSize)
        val partR = initPartR(d, batchSize)
        var counter = 0
        var totalCounter = 0L
        var batchesCounter = 0
        val timer = Timer()

        val flow = flow {
            for (i in 0 until n - 1) {
                timer.start()
                y[1] = method(system, x, y[0], dx)
                val duration = timer.stop()
                if (counter == batchSize) {
                    val spentTime = timer.total()
                    batchesCounter++
                    this.emit(partT to partR)
                    println(MessageUtils.emitionMessage(method, batchesCounter, duration, spentTime, n, totalCounter))
                    counter = 0
                } else {
                    partT[counter] = x
                    partR.withIndex().forEach {
                        it.value[counter] = y[1][it.index]
                    }
                    counter++
                    totalCounter++
                }
                x += dx
                y[0] = y[1]
            }

            if (partT.isNotEmpty()) {
                this.emit(partT to partR)
                println("The last batch with size '${partT.size}' was emitted")
            }
            println("Batch flow ended its emissions. Whole duration: ${formatMillis(timer.total())}")
        }

        return SolutionBatchFlow(this.method.name, DoubleDataSets(dataSets), flow)
    }

    private fun initY(d: Int, y0: Y): Array<Array<Double>> {
        val y = ArrayUtils.twoDimArray(2 to d)
        (0 until d).forEach { i -> y[0][i] = y0[i] }
        return y
    }

    private fun getStepAndSteps(): Pair<dX, N> {
        val dx = computationConfigs.dx
        val n = computationConfigs.n
        return Pair(dx, n)
    }

    private fun getInitialPoint(): Pair<X, Y> {
        val x0 = initialPoint.x0
        val y0 = initialPoint.y0
        return Pair(x0, y0)
    }

    private fun initPartR(d: D, batchSize: Int) = InitUtils.doubleArrays(d, batchSize)
    private fun initPartT(batchSize: Int) = DoubleArray(batchSize)
}