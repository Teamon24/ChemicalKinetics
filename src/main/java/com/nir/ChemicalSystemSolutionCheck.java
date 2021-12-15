package com.nir;

import com.nir.beans.method.Methods;
import com.nir.beans.reaction.ChemicalReactionComponent;
import com.nir.ui.pojos.ReactionStage;
import com.nir.ChemicalReaction;
import com.nir.utils.PlatformUtils;
import com.nir.utils.PlotUtils;
import com.nir.utils.math.ComputationConfigs;
import com.nir.utils.math.InitialPoint;
import com.nir.beans.method.Method;
import com.nir.utils.math.solution.Solution;
import com.nir.utils.math.F;
import com.nir.utils.math.solution.SolutionBatchFlow;
import com.nir.utils.math.solution.SolutionFlow;
import com.nir.utils.math.solution.TaskStep;
import de.gsi.chart.XYChart;
import de.gsi.dataset.spi.DoubleDataSet;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;

public class ChemicalSystemSolutionCheck extends Application {
    final static Random random = new Random();

    static {
        random.setSeed(15);
    }

    @Override
    public void start(Stage stage) {

        //Берем одну из реакций и
        final ChemicalReaction chemicalReaction = ChemicalReaction.chemicalReaction2();
        final List<ReactionStage> reactionStages = chemicalReaction.getReactionStages();

        //Подготовка объектов с данными решения системы
        //и открытие пустых графиков
        final List<String> compounds = ChemicalReactionComponent.getCompounds(reactionStages);
        final List<DoubleDataSet> dataSets = PlotUtils.dataSets(compounds);
        final List<XYChart> charts = PlotUtils.charts(dataSets);
        PlotUtils.show(charts, stage);

        //Выбор вычислительного метода
        final InitialPoint initialPoint = chemicalReaction.getInitialPoint();
        final ComputationConfigs computationConfigs = chemicalReaction.getComputationConfigs();
        final Method method = Methods
            .getByName("Runge-Kutta 4th-order: v.1")
            .setUp(initialPoint, computationConfigs);

        //Составление объекта с настройками решения
        final F system = ChemicalReactionComponent.getSystem(chemicalReaction);
        TaskStep datasets = Solution
            .method(method)
            .computation(computationConfigs)
            .system(system)
            .initialPoint(initialPoint)
            .datasets(dataSets);

        final Runnable solution = datasets.futureTask();
        final SolutionFlow flow = datasets.flow();
        final SolutionBatchFlow batchFlow = datasets.batchFlow(20);

        //Запуск решения системы уравнений - на этом моменте времене в графики будут добавляться точки решения
        PlatformUtils.runLater(batchFlow);
    }



    public static void main(String[] args) {
        launch();
    }
}
