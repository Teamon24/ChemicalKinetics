package com.nir.utils;

import com.nir.utils.math.InitialData;
import com.nir.utils.math.Method;
import com.nir.utils.math.RungeKutta;
import com.nir.utils.math.Solution;
import de.gsi.chart.XYChart;
import de.gsi.dataset.spi.DoubleDataSet;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SolutionFlowPlotCheck extends Application {
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
        final InitialData initialData = system.initialData();
        final Method method = new RungeKutta(4, initialData);
        final SolutionFlow solutionFlow =
            Solution
                .method(method)
                .system(system)
                .initialData(initialData)
                .datasets(dataSets)
                .flow();

        PlatformUtils.runLater(solutionFlow);
    }

    public static Collector<String, ?, ArrayList<String>> arrayList() {
        return Collectors.toCollection(ArrayList::new);
    }

    public static void main(String[] args) {
        launch();
    }
}
