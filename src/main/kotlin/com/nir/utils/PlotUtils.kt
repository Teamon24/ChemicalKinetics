package com.nir.utils

import com.nir.ui.UiComponents
import com.nir.utils.PlatformUtils.runLater
import com.nir.utils.math.solution.SolutionFlow
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
    fun <T> show(stage: Stage, charts: List<XYChart>, solutionFlow: SolutionFlow<T>) {
        val root = StackPane()
        val flowPane = flowPane()
        val scrollPane = scrollPane(flowPane)
        val periodicTableButton = getPeriodicTableButton()
        val computationButton = computationButton(solutionFlow)

        flowPane.children.addAll(charts)
        flowPane.children.add(periodicTableButton)
        flowPane.children.add(computationButton)

        root.children.add(scrollPane)

        val scene = Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT)
        stage.scene = scene
        stage.setOnCloseRequest { exitProcess(0) }
        stage.show()
    }

    private fun <T> computationButton(solutionFlow: SolutionFlow<T>): Button {
        val computationButton = Button()
            .apply {
                this.text = "Computation"
                this.onAction = EventHandler {
                    this.isDisable = true
                    runLater(solutionFlow, onComplete = { this.isDisable = false })
                }
            }
        return computationButton
    }

    private fun flowPane(): FlowPane {
        val flowPane = FlowPane()
        flowPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT)
        return flowPane
    }

    private fun scrollPane(flowPane: FlowPane): ScrollPane {
        val scrollPane = ScrollPane()
        scrollPane.content = flowPane
        scrollPane.isFitToWidth = true
        scrollPane.isFitToHeight = true
        return scrollPane
    }

    private fun getPeriodicTableButton(): Button {
        val periodicTableButton = Button()
            .apply {
                this.text = "Periodic Table"
                this.onAction = EventHandler {
                    UiComponents.periodicElementsStage().show()
                }
            }
        return periodicTableButton
    }
}