package com.nir.utils;

import com.nir.beans.Methods;
import com.nir.utils.math.ComputationConfigs;
import com.nir.utils.math.InitialPoint;
import com.nir.utils.math.method.automatized.Method;
import com.nir.utils.math.method.hardcoded.ForwardEuler;
import com.nir.utils.math.method.hardcoded.RungeKutta;
import com.nir.utils.math.solution.Solution;
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
        final ComputationConfigs computationConfigs = new ComputationConfigs(0.000001, 5_000_000);
        final Method adamBashGeneral = Methods.getByName("Adams-Bashforth 1-order method");
        final Method rungeKutta4General = Methods.getByName("Runge-Kutta 4th-order: v.1");
        final Method rungeKutta5General = Methods.getByName("Runge-Kutta 5th-order method");

        final Method eulerGeneral = Methods.getByName("Forward Euler");
        final RungeKutta rungeKutta4 = new RungeKutta(4);
        final RungeKutta rungeKutta5 = new RungeKutta(5);
        final Method method = ForwardEuler.INSTANCE.setUp(initialPoint, computationConfigs);


        System.out.println(String.format("Numeric Method: \"%s\"", method.getName()));

        final Runnable solution =
            Solution
                .method(method)
                .computation(computationConfigs)
                .system(system)
                .initialData(initialPoint)
                .datasets(dataSets)
                .task();

        PlatformUtils.runLater(solution);
    }

    public static void main(String[] args) {
        launch();
    }
}
