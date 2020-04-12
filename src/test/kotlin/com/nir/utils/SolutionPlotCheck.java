package com.nir.utils;

import com.nir.beans.Methods;
import com.nir.utils.math.InitialData;
import com.nir.utils.math.method.Method;
import com.nir.utils.math.solution.Solution;
import com.nir.utils.math.solution.SolutionBatchFlow;
import de.gsi.chart.XYChart;
import de.gsi.dataset.spi.DoubleDataSet;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class SolutionPlotCheck extends Application {
    @Override
    public void start(Stage stage) {

        //Система уравнений
        final LorentzStrangeAttractor2 system = new LorentzStrangeAttractor2();
        final List<String> titles = system.titles();

        //Подготовка объектов для данных с решением системы
        //и открытие графиков
        final List<DoubleDataSet> dataSets = PlotUtils.dataSets(titles);
        final List<XYChart> charts = PlotUtils.charts(dataSets);
        PlotUtils.show(charts, stage);

        //Запуск решения системы уравнений
        final InitialData initialData = system.initialData();
        final double dx = 0.000002;
        final Method method = Methods.getByName("Runge-Kutta 4th-order: v.2");
        method.set(dx);
        System.out.println(String.format("Numeric Method: \"%s\"", method.getName()));

        final SolutionBatchFlow solution =
            Solution
                .method(method)
                .computation(dx, 222_000)
                .system(system)
                .initialData(initialData)
                .datasets(dataSets)
                .batchFlow(10_000);

        PlatformUtils.runLater(solution);
    }

    public static void main(String[] args) {
        launch();
    }
}
