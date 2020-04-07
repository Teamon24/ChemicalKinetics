package com.nir.utils

import com.nir.ui.UiComponents
import com.nir.utils.math.method.N
import com.nir.utils.math.method.X0
import com.nir.utils.math.method.dX
import de.gsi.chart.XYChart
import de.gsi.chart.axes.spi.DefaultNumericAxis
import de.gsi.dataset.spi.DoubleDataSet
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import kotlin.system.exitProcess


object PlotUtils {

    private const val WINDOW_WIDTH = 800.0
    private const val WINDOW_HEIGHT = 600.0

    @JvmStatic
    fun dataSets(titles: List<String>): List<DoubleDataSet> {
        return titles.map { title -> DoubleDataSet(title) }
    }

    @JvmStatic
    fun charts(dataSets: List<DoubleDataSet>): List<XYChart> {

        val charts = dataSets.map { dataSet ->
            val chart = XYChart(DefaultNumericAxis(), DefaultNumericAxis())
            chart.datasets.add(dataSet)
            chart.setPrefSize(WINDOW_HEIGHT, WINDOW_HEIGHT)
            chart.styleClass.add("solution-chart")
            chart
        }

        return charts
    }

    @JvmStatic
    fun show(charts: List<XYChart>, stage: Stage) {

        val root = StackPane()
        val scrollPane = ScrollPane()
        val flowPane = FlowPane()
        flowPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT)
        scrollPane.content = flowPane
        scrollPane.isFitToWidth = true
        scrollPane.isFitToHeight = true

        val button = Button().apply {
            this.text = "Periodic Table"
            this.onAction = EventHandler {
                UiComponents.periodicElementsStage().show()
            }
        }
        flowPane.children.addAll(charts)
        flowPane.children.add(button)

        root.children.add(scrollPane)
        val scene = Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT)

        stage.scene = scene
        stage.setOnCloseRequest { exitProcess(0) }
        stage.show()
    }

    fun series(x0: X0, n: N, dx: dX): DoubleArray {
        val series = DoubleArray(n)
        var t = x0
        for (i in 0 until n) {
            series[i] = t
            t += dx
        }
        return series
    }

}