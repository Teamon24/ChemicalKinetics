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
        final List<String> methodsName = Methods.getNames();
        final String methodName = RandomUtils.randomIn(methodsName);
        System.out.println(String.format("Numeric Method: \"%s\"", methodName));
        final Method method = Methods.getByName(methodName, initialData);

        final SolutionBatchFlow solution =
            Solution
                .method(method)
                .system(system)
                .initialData(initialData)
                .datasets(dataSets)
                .batchFlow();

        PlatformUtils.runLater(solution);
    }

    public static void main(String[] args) {
        launch();
    }
}
