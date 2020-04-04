package com.nir.utils;

import com.nir.beans.StageParser;
import com.nir.beans.StehiomatrixGetter;
import com.nir.utils.math.InitialData;
import com.nir.utils.math.Method;
import com.nir.utils.math.RungeKutta;
import com.nir.utils.math.Solution;
import com.nir.utils.math.StageRates;
import com.nir.utils.math.Stehiomatrix;
import com.nir.utils.math.System;
import de.gsi.chart.XYChart;
import de.gsi.dataset.spi.DoubleDataSet;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChemicalSystemSolutionCheck extends Application {
    final static Random random = new Random();
    static {
        random.setSeed(15);
    }
    @Override
    public void start(Stage stage) {

        //Берем одну из реакций
        final ChemicalReaction chemicalReaction = ChemicalReaction.chemicalReaction1();
        final Stream<String> reaction = chemicalReaction.getReaction();

        final List<com.nir.ui.dto.Stage> stages =
            reaction
            .map(StageParser::parse)
            .map(StageParser::convert)
            .collect(Collectors.toList());


        //Подготовка объектов с данными решения системы
        //и открытие пустых графиков
        final List<String> compounds = StehiomatrixGetter.getCompounds(stages);
        final List<DoubleDataSet> dataSets = PlotUtils.dataSets(compounds);
        final List<XYChart> charts = PlotUtils.charts(dataSets);
        PlotUtils.show(charts, stage);

        //Составление объекта с настройками решения
        final InitialData initialData = chemicalReaction.initialData();
        final Method method = new RungeKutta(4, initialData);
        final System system = getSystem(chemicalReaction, stages);
        final SolutionFlow solutionFlow =
            Solution
                .method(method)
                .system(system)
                .initialData(initialData)
                .datasets(dataSets)
                .flow();

        //Запуск решения системы уравнений - на этом моменте времене в графики будут добавляться точки решения
        PlatformUtils.runLater(solutionFlow);
    }

    private System getSystem(ChemicalReaction chemicalReaction, List<com.nir.ui.dto.Stage> stages) {
        final Stehiomatrix matrix = StehiomatrixGetter.getMatrix(stages);
        Double[] k = chemicalReaction.getK();
        final StageRates rates = StehiomatrixGetter.getRates(stages, k);
        return StehiomatrixGetter.times(matrix.transpose(), rates);
    }

    public static void main(String[] args) {
        launch();
    }

}
