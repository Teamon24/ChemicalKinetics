package com.nir;

import com.google.common.collect.Lists;
import com.nir.beans.method.hardcoded.RungeKutta;
import com.nir.utils.LorentzStrangeAttractor;
import com.nir.utils.PlatformUtils;
import com.nir.utils.PlotUtils;
import com.nir.utils.math.ComputationConfigs;
import com.nir.utils.math.InitialPoint;
import com.nir.utils.math.solution.Solution;
import com.nir.utils.math.solution.SolutionFlow;
import de.gsi.chart.XYChart;
import de.gsi.dataset.spi.DoubleDataSet;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SolutionPlotCheck extends Application {
    @Override
    public void start(Stage stage) {

        final LorentzStrangeAttractor system = new LorentzStrangeAttractor();


        final ArrayList<String> methods = Lists.newArrayList(
            "Runge-Kutta 4th-order: v.1",
            "Runge-Kutta 5th-order method",
            "Forward Euler",
            new RungeKutta(4).getName(),
            new RungeKutta(5).getName()
        );

        double dx = 0.001;
        int N = 100000;

        final ComputationConfigs computationConfigs = new ComputationConfigs(dx, N);
        final InitialPoint initialPoint = system.initialPoint();

        final List<DoubleDataSet> dataSets = PlotUtils.dataSets(system.titles());
        final List<XYChart> charts = PlotUtils.charts(dataSets);

        SolutionFlow<?> flow = getSolutionFlow(
            system, dataSets, computationConfigs, initialPoint, methods.get(1));


        //Запуск решения системы уравнений -
        //на этом моменте времене в графики будут добавляться точки решения
        PlotUtils.show(stage, charts, flow);
    }

    @NotNull
    private SolutionFlow<?> getSolutionFlow(
        LorentzStrangeAttractor system,
        List<DoubleDataSet> dataSets,
        ComputationConfigs computationConfigs,
        InitialPoint initialPoint,
        String methodName)
    {
        System.out.println(String.format("Numeric Method: \"%s\"", methodName));

        SolutionFlow<?> flow = Solution
            .system(system)
            .initialPoint(initialPoint)
            .computation(computationConfigs)
            .method(methodName)
            .datasets(dataSets)
            .batchFlow(100);

        return flow;
    }

    public static void main(String[] args) {
        launch();
    }
}
