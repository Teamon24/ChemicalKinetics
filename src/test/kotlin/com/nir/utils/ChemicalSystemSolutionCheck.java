package com.nir.utils;

import com.nir.beans.Methods;
import com.nir.beans.StageParser;
import com.nir.beans.StehiomatrixGetter;
import com.nir.ui.pojos.ReactionStage;
import com.nir.utils.math.ComputationConfigs;
import com.nir.utils.math.InitialPoint;
import com.nir.utils.math.method.Method;
import com.nir.utils.math.solution.Solution;
import com.nir.ui.pojos.StageRates;
import com.nir.utils.math.Matrix;
import com.nir.utils.math.method.F;
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
        final ChemicalReaction chemicalReaction = ChemicalReaction.chemicalReaction2();
        final Stream<String> reaction = chemicalReaction.getReaction();

        final List<ReactionStage> reactionStages =
            reaction
            .map(StageParser::parse)
            .map(StageParser::convert)
            .collect(Collectors.toList());


        //Подготовка объектов с данными решения системы
        //и открытие пустых графиков
        final List<String> compounds = StehiomatrixGetter.getCompounds(reactionStages);
        final List<DoubleDataSet> dataSets = PlotUtils.dataSets(compounds);
        final List<XYChart> charts = PlotUtils.charts(dataSets);
        PlotUtils.show(charts, stage);

        //Выбор вычислительного метода
        final InitialPoint initialPoint = chemicalReaction.getInitialData();
        final ComputationConfigs computationConfigs = chemicalReaction.getComputationConfigs();
        final Method method = Methods
            .getByName("Runge-Kutta 4th-order: v.1")
            .init(initialPoint, computationConfigs);

        //Составление объекта с настройками решения
        final F system = getSystem(chemicalReaction, reactionStages);
        final Runnable solutionFlow =
            Solution
                .method(method)
                .computation(computationConfigs)
                .system(system)
                .initialData(initialPoint)
                .datasets(dataSets)
                .task();

        //Запуск решения системы уравнений - на этом моменте времене в графики будут добавляться точки решения
        PlatformUtils.runLater(solutionFlow);
    }

    private F getSystem(ChemicalReaction chemicalReaction, List<ReactionStage> reactionStages) {
        final Matrix<Integer> matrix = StehiomatrixGetter.getMatrix(reactionStages);
        Double[] k = chemicalReaction.getK();
        final StageRates rates = StehiomatrixGetter.getRates(reactionStages, k);
        return StehiomatrixGetter.times(matrix.transpose(), rates);
    }

    public static void main(String[] args) {
        launch();
    }

}
