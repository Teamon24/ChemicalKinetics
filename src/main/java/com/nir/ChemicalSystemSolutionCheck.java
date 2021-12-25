package com.nir;

import com.nir.beans.reaction.ChemicalReactionComponent;
import com.nir.ui.pojos.ReactionStage;
import com.nir.utils.PlatformUtils;
import com.nir.utils.PlotUtils;
import com.nir.utils.math.ComputationConfigs;
import com.nir.utils.math.InitialPoint;
import com.nir.utils.math.solution.Solution;
import com.nir.utils.math.F;
import com.nir.utils.math.solution.SolutionBatchFlow;
import com.nir.utils.math.solution.SolutionListFlow;
import com.nir.utils.math.solution.SolutionFlowImpl;
import com.nir.utils.math.solution.FlowTypeStep;
import de.gsi.chart.XYChart;
import de.gsi.dataset.spi.DoubleDataSet;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class ChemicalSystemSolutionCheck extends Application {

    @Override
    public void start(Stage stage) {

        final ChemicalReaction chemicalReaction = ChemicalReaction.chemicalReaction2();
        final List<ReactionStage> reactionStages = chemicalReaction.getReactionStages();

        final List<String> compounds = ChemicalReactionComponent.getCompounds(reactionStages);
        final List<DoubleDataSet> dataSets = PlotUtils.dataSets(compounds);
        final List<XYChart> charts = PlotUtils.charts(dataSets);

        //Открытие меню

        //Выбор вычислительного метода
        final InitialPoint initialPoint = chemicalReaction.getInitialPoint();
        final ComputationConfigs computationConfigs = chemicalReaction.getComputationConfigs();

        final F system = ChemicalReactionComponent.getSystem(chemicalReaction);
        final FlowTypeStep flowTypeStep = Solution
            .system(system)
            .initialPoint(initialPoint)
            .computation(computationConfigs)
            .method("Runge-Kutta 4th-order: v.1")
            .datasets(dataSets);

        final Runnable solution = flowTypeStep.futureTask();
        final SolutionFlowImpl flow = flowTypeStep.flow();
        final SolutionBatchFlow batchFlow = flowTypeStep.batchFlow(20);
        final SolutionListFlow listFlow = flowTypeStep.listFlow(20);

        //Запуск решения системы уравнений - на этом моменте времене в графики будут добавляться точки решения
        PlotUtils.show(stage, charts, listFlow);
    }

    public static void main(String[] args) {
        launch();
    }
}
