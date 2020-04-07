package com.nir.utils.math

import com.nir.utils.PlotUtils
import com.nir.utils.SolutionFlow
import com.nir.utils.SolutionPartialFlow
import de.gsi.dataset.spi.DoubleDataSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

object Solution {

    class Info internal constructor() {
        internal lateinit var method: Method
        internal lateinit var system: System
        internal lateinit var initialData: InitialData
        internal lateinit var dataSets: List<DoubleDataSet>
    }

    @JvmStatic
    fun method(method: Method): SystemStep {
        val info = Info()
        info.method = method
        return SystemStep(info)
    }

    class SystemStep(private val info: Info) {
        fun system(system: System): InitialDataStep {
            this.info.system = system
            return InitialDataStep(this.info);
        }
    }

    class InitialDataStep(private val info: Info) {

        fun initialData(initialData: InitialData): DataSetsStep {
            this.info.initialData = initialData
            return DataSetsStep(this.info)
        }
    }

    class DataSetsStep(private val info: Info) {

        fun datasets(dataSets: List<DoubleDataSet>): TypeStep {
            this.info.dataSets = dataSets
            return TypeStep(this.info)
        }
    }

    class TypeStep(private val info: Info) {


        fun task(): Runnable {
            return Runnable {
                val (t0, r0, dt, N) = info.initialData
                val system = info.system
                val method = info.method
                val series = PlotUtils.series(t0, N, dt)
                val solution = method(system, r0, t0, dt, N)
                this.info.dataSets.withIndex().forEach { (index, dataSet) ->
                    dataSet.add(series, solution.map { it[index] }.toDoubleArray())
                }
            }
        }

        fun flow(): SolutionFlow {
            val flow = flow {
                val (t0, r0, dt, N) = info.initialData
                val system = info.system
                val method = info.method
                val D = r0.size
                var t = t0

                val r = ArrayUtils.twoDimArray(2 to D)
                (0 until D).forEach { i -> r[0][i] = r0[i] }

                for (i in 0 until N - 1) {
                    r[1] = method(system, r[0], t, dt)

                    if (i % 15 == 0) {
                        delay(1)
                    }

                    this.emit(t to r[1])
                    t += dt
                    r[0] = r[1]
                }
            }
            return SolutionFlow(this.info.dataSets, flow)
        }

        fun partialFlow(partial: Int): SolutionPartialFlow {
            val flow = flow {
                val (t0, r0, dt, N) = info.initialData
                val system = info.system
                val method = info.method
                val D = r0.size
                var t = t0

                val r = ArrayUtils.twoDimArray(2 to D)
                (0 until D).forEach { i -> r[0][i] = r0[i] }

                var counter = 0;

                val part = ArrayList<Pair<T, R>>(partial)
                for (i in 0 until N - 1) {
                    r[1] = method(system, r[0], t, dt)

                    if (counter == partial) {
                        this.emit(part)
                        counter == 0
                        part.clear()
                    } else {
                        part.add(t to r[1])
                        counter++
                    }
                    t += dt
                    r[0] = r[1]
                }
            }
            return SolutionPartialFlow(this.info.dataSets, flow)
        }

        private fun rToStr(r: Array<Double>) = r.withIndex().joinToString(separator = ", ", truncated = "") { (index, value) -> "r${index + 1} = $value" }

    }

}




