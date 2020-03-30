package com.nir.utils

import de.gsi.chart.XYChart
import de.gsi.chart.axes.spi.DefaultNumericAxis
import de.gsi.dataset.spi.DoubleDataSet
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import kotlin.system.exitProcess


data class InitialData(
        val t0: Double = 0.0,
        val r0: Array<Double> = arrayOf(1.0, 1.0, 1.0),
        val dt: Double = 0.01,
        val N: Int = 15_000
)

object PlotUtils {

    fun show(titles: List<String>, stage: Stage): List<DoubleDataSet> {
        val WINDOW_WIDTH = 800.0
        val WINDOW_HEIGHT = 600.0

        val root = StackPane()
        val scrollPane = ScrollPane()
        val flowPane = FlowPane()
        flowPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT)
        scrollPane.content = flowPane
        scrollPane.isFitToWidth = true
        scrollPane.isFitToHeight = true
        val dataSets = titles.map { title -> DoubleDataSet(title) }


        dataSets.forEach { dataSet ->
            val chart = XYChart(DefaultNumericAxis(), DefaultNumericAxis())
            chart.datasets.add(dataSet)
            chart.setPrefSize(WINDOW_HEIGHT, WINDOW_HEIGHT)
            chart.styleClass.add("solution-chart")
            flowPane.children.add(chart)
        }

        root.children.add(scrollPane)
        val scene = Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT)

        stage.scene = scene
        stage.setOnCloseRequest { exitProcess(0) }
        stage.show()
        return dataSets
    }

    fun series(t0: Double, n: Int, dt: Double): DoubleArray {
        val series = DoubleArray(n)
        var t = t0
        for (i in 0 until n) {
            series[i] = t
            t += dt
        }
        return series
    }

}