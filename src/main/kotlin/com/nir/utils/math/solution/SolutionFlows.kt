package com.nir.utils.math.solution

import com.nir.utils.math.XY
import com.nir.utils.math.XsYs
import de.gsi.dataset.spi.DoubleDataSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

interface SolutionFlow {
    suspend fun collect()
}

interface PointsStorage {
    fun addPoints(points: XsYs)
    fun addPoint(point: XY)
}

class DoubleDataSets(private var dataSets: List<DoubleDataSet>): PointsStorage {
    override fun addPoints(points: XsYs) {
        dataSets.withIndex().forEach { (index, dataSet) ->
            val xs = points.first
            val ys = points.second[index]
            dataSet.add(xs, ys)
        }
    }

    override fun addPoint(point: XY) {
        dataSets.withIndex().forEach { (index, dataSet) ->
            val xs = point.first
            val ys = point.second[index]
            dataSet.add(xs, ys)
        }
    }

}

class SolutionFlowImpl(
    private val collectTo: PointsStorage,
    private val flow: Flow<XY>): SolutionFlow
{

    override suspend fun collect() {
        this.flow.collect { point -> collectTo.addPoint(point) }
    }
}

class SolutionBatchFlow(
    private val collectTo: PointsStorage,
    private val flow: Flow<XsYs>): SolutionFlow
{

    override suspend fun collect() {
        this.flow.collect { point -> collectTo.addPoints(point) }
    }
}
