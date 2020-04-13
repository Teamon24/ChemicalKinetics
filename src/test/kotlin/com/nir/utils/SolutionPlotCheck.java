package com.nir.utils;

import com.nir.beans.Methods;
import com.nir.utils.math.ComputationConfigs;
import com.nir.utils.math.InitialPoint;
import com.nir.utils.math.method.Method;
import com.nir.utils.math.method.deprecated.RungeKutta;
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
        final InitialPoint initialPoint = system.initialPoint();
        final ComputationConfigs computationConfigs = new ComputationConfigs(0.000001, 5_222_000);
        final String adamBashName = "Adams-Bashforth 1-order method";
        final String rungeKuttaName = "Runge-Kutta 4th-order: v.1";
        final String eulerName = "Forward Euler";
        final RungeKutta rungeKutta = new RungeKutta(4);
        final Method byName = Methods.getByName(rungeKuttaName);
        final Method method = byName.init(initialPoint, computationConfigs);


        System.out.println(String.format("Numeric Method: \"%s\"", method.getName()));

        final SolutionBatchFlow solution =
            Solution
                .method(method)
                .computation(computationConfigs)
                .system(system)
                .initialData(initialPoint)
                .datasets(dataSets)
                .batchFlow(50_000);

        PlatformUtils.runLater(solution);
    }

    public static void main(String[] args) {
        launch();
    }
}
