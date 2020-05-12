package com.nir

import de.gsi.chart.XYChart
import de.gsi.chart.axes.spi.DefaultNumericAxis
import de.gsi.dataset.spi.DoubleDataSet
import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.control.ScrollPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Screen
import kotlin.system.exitProcess


object PlotUtils {


    @JvmStatic
    fun dataSets(titles: List<String>): List<DoubleDataSet> {
        return titles.map { title -> DoubleDataSet(title) }
    }

    @JvmStatic
    fun charts(dataSets: List<DoubleDataSet>): List<XYChart> {

        val charts = dataSets.map { dataSet ->
            val chart = XYChart(DefaultNumericAxis(), DefaultNumericAxis())
            chart.datasets.add(dataSet)
            val screen = Screen.getPrimary()
            val bounds = screen.visualBounds;
            chart.setPrefSize(bounds.width / 2 - 50, bounds.height / 2 - 50)
            chart.styleClass.add("solution-chart")
            chart
        }

        return charts
    }

    @JvmStatic
    fun show(charts: List<XYChart>, main2: Main2) {
        val stackPane = StackPane()
        val root = VBox()
        stackPane.children.add(root)
        val scrollPane = ScrollPane()
        val flowPane = FlowPane()

//        flowPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT)
        scrollPane.content = flowPane
        scrollPane.isFitToWidth = true
        scrollPane.isFitToHeight = true
        flowPane.children.addAll(charts)

        val menuBar = MenuBar()
        val mainMenu = Menu("Меню")
        val mainItems = ArrayList<MenuItem>(2)
        mainItems.add(Menu("Параметры моделирования"))
        mainItems.add(Menu("Справка"))
        mainItems.add(Menu("Выход"))
        mainMenu.items.addAll(mainItems)
        menuBar.menus.add(mainMenu)
        mainMenu.items[0].setOnAction {
            main2.mainStage.hide();
        }
        mainMenu.items[1].setOnAction {
            exitProcess(0)
        }
        mainMenu.items[2].setOnAction {
            exitProcess(0)
        }
        root.children.add(menuBar)
        root.children.add(scrollPane)

        val scene = Scene(stackPane)
        main2.mainStage.scene = scene
        main2.mainStage.show()
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