package com.nir.ui.utils

import com.nir.ui.utils.math.RungeKutta
import com.nir.ui.utils.math.RungeKuttaFlow
import de.gsi.chart.XYChart
import de.gsi.chart.axes.spi.DefaultNumericAxis
import de.gsi.dataset.spi.DoubleDataSet
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


class SolutionApp {

    fun start(primaryStage: Stage) {
        val N = 55_000
        val t0 = 0.0
        val dt = 0.0055
        val r0 = arrayOf(1.0, 1.0, 1.0)

        val solutionFlow = RungeKuttaFlow(4).flow(LorentzStrangeAttractor(), r0, t0, N, dt)

        val root = StackPane()
        val chart = XYChart(DefaultNumericAxis(), DefaultNumericAxis())
        root.children.add(chart)

        val xDatas = DoubleDataSet("x points")
        val yDatas = DoubleDataSet("y points")
        val zDatas = DoubleDataSet("z points")

        chart.datasets.addAll(xDatas, yDatas, zDatas)
        val scene = Scene(root, 800.0, 600.0)

        primaryStage.scene = scene;
        primaryStage.setOnCloseRequest { exitProcess(0) }
        primaryStage.show();

        Platform.runLater() {
            CoroutineScope(Dispatchers.IO).launch {
                solutionFlow.collect { value ->
                    val xs = value.first[0]
                    val ys = value.first[1]
                    val zs = value.first[2]

                    xDatas.add(value.second, xs)
                    yDatas.add(value.second, ys)
                    zDatas.add(value.second, zs)
                }
            }
        }
    }
}

