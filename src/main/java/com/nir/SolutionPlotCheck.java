package com.nir;

import com.google.common.collect.Lists;
import com.nir.beans.method.Methods;
import com.nir.utils.LorentzStrangeAttractor;
import com.nir.utils.PlatformUtils;
import com.nir.utils.PlotUtils;
import com.nir.utils.math.ComputationConfigs;
import com.nir.beans.method.Method;
import com.nir.beans.method.hardcoded.RungeKutta;
import com.nir.utils.math.InitialPoint;
import com.nir.utils.math.solution.Solution;
import com.nir.utils.math.solution.SolutionBatchFlow;
import com.nir.utils.math.solution.SolutionFlow;
import com.nir.utils.math.solution.TaskStep;
import de.gsi.chart.XYChart;
import de.gsi.dataset.spi.DoubleDataSet;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SolutionPlotCheck extends Application {
    @Override
    public void start(Stage stage) {

        //Система уравнений
        final LorentzStrangeAttractor system = new LorentzStrangeAttractor();

        //Подготовка объектов для данных с решением системы
        //и открытие графиков
        final List<DoubleDataSet> dataSets = PlotUtils.dataSets(system.titles());
        final List<XYChart> charts = PlotUtils.charts(dataSets);

        PlotUtils.show(charts, stage);

        final ArrayList<Method> methods = Lists.newArrayList(
            Methods.getByName("Runge-Kutta 4th-order: v.1 (Generalized)"),
            Methods.getByName("Runge-Kutta 5th-order method (Generalized)"),
            Methods.getByName("Forward Euler (Generalized)"),
            new RungeKutta(4),
            new RungeKutta(5)
        );

        double dx = 0.000001;
        int N = 2000000;

        final ComputationConfigs computationConfigs = new ComputationConfigs(dx, N);
        final InitialPoint initialPoint = system.initialPoint();

        final Method method = methods.get(3);
        method.setUp(initialPoint, computationConfigs);

        System.out.println(String.format("Numeric Method: \"%s\"", method.getName()));


        TaskStep datasets = Solution
            .method(method)
            .computation(computationConfigs)
            .system(system)
            .initialPoint(initialPoint)
            .datasets(dataSets);

        final Runnable solution = datasets.futureTask();
        final SolutionFlow flow = datasets.flow();
        final SolutionBatchFlow batchFlow = datasets.batchFlow(20);

        PlatformUtils.runLater(batchFlow);
    }

    public static void main(String[] args) {
        launch();
    }
}
