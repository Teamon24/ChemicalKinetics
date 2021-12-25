package com.nir.utils.math.solution

import com.nir.utils.math.X
import com.nir.utils.math.XY
import com.nir.utils.math.XsYs
import de.gsi.dataset.spi.DoubleDataSet

interface PointsStorage {
    fun addPoints(points: XsYs)
    fun addPoints(points: List<XY>)
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

    override fun addPoints(points: List<XY>) {
        val xs = points.map { it.first }.toDoubleArray()
        val ys = points.map { it.second.toDoubleArray() }.toTypedArray()
        dataSets.withIndex().forEach { (index, dataSet) ->
            val ys2 = ys.map { it[index] }.toDoubleArray()
            dataSet.add(xs, ys2)
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