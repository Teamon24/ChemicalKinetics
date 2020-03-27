package com.nir.ui.utils

import com.nir.ui.utils.math.RungeKutta
import org.knowm.xchart.XChartPanel
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.XYSeries
import org.knowm.xchart.style.Styler
import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.SwingUtilities

fun main() {
    val N = 30000
    val t0 = 0.0
    val dt = 0.0025
    val solution = RungeKutta(4)(LorentzStrangeAttractor(), arrayOf(1.0, 1.0, 1.0), t0, N, dt)

    val chart = XYChartBuilder().width(800).height(600).theme(Styler.ChartTheme.Matlab).build()
    val timeSeries = series(t0, N, dt)
    chart.addSeries("x", timeSeries, solution.map { it[0] }.toDoubleArray())
    chart.addSeries("y", timeSeries, solution.map { it[1] }.toDoubleArray())
    chart.addSeries("z", timeSeries, solution.map { it[2] }.toDoubleArray())

    chart.styler.defaultSeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Scatter
    chart.styler.markerSize = 1;

    SwingUtilities.invokeLater {
        val frame = JFrame("Advanced Example")
        frame.layout = BorderLayout()
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val chartPanel: JPanel = XChartPanel<XYChart>(chart)
        frame.add(chartPanel, BorderLayout.CENTER)
        val label = JLabel("Blah blah blah.", SwingConstants.CENTER)
        frame.add(label, BorderLayout.SOUTH)
        frame.pack()
        frame.isVisible = true
    }
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