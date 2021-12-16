package com.nir;

import com.nir.beans.method.hardcoded.AdamBashforthMethod;
import com.nir.utils.LorentzStrangeAttractor;
import com.nir.utils.PlatformUtils;
import com.nir.utils.PlotUtils;
import com.nir.utils.math.ComputationConfigs;
import com.nir.utils.math.InitialPoint;
import com.nir.beans.method.hardcoded.AdamBashforth4thMethods;
import com.nir.utils.math.solution.Solution;
import de.gsi.chart.XYChart;
import de.gsi.dataset.spi.DoubleDataSet;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class AdamBashforthSolutionPlotCheck extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {

        //Система уравнений
        final LorentzStrangeAttractor system = new LorentzStrangeAttractor();
        final List<String> titles = system.titles();

        //Подготовка объектов для данных с решением системы
        //и открытие графиков
        final List<DoubleDataSet> dataSets = PlotUtils.dataSets(titles);
        final List<XYChart> charts = PlotUtils.charts(dataSets);
        PlotUtils.show(charts, stage);

        //Запуск решения системы уравнений
        final InitialPoint initialPoint = system.initialPoint();
        final ComputationConfigs computationConfigs = new ComputationConfigs(0.00001, 2_000_000);

        String methodName = "Adam-Bashforth Method of ${order}-order (Hardcoded)";
        System.out.println(String.format("Numeric Method: \"%s\"", methodName));

        final Runnable solution =
            Solution
                .system(system)
                .initialPoint(initialPoint)
                .computation(computationConfigs)
                .method(
                    methodName,
                    (method) -> ((AdamBashforthMethod) method)
                        .setFirstAccelerationPointMethodName("Runge-Kutta 4-order (Generalized)")
                )
                .datasets(dataSets)
                .futureTask();

        PlatformUtils.runLater(solution);
    }
}
