package com.nir.utils;

import com.nir.beans.StageParser;
import com.nir.beans.StehiomatrixGetter;
import com.nir.utils.math.F;
import com.nir.utils.math.StageRates;
import com.nir.utils.math.Stehiomatrix;
import com.nir.utils.math.flow.EulerFlow;
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

        final int n = 15000;
        final double dt = 0.0002;
        final Double[] k = new Double[]{ 1.0, 1.0, 15.0, 1.0, 1.0 };
        final Double[] r0 = { 5.5, 0.0, 12.0, 0.0, 50.0 };
        final Stream<String> reaction = Stream.of(
            "Br2->2Br",
            "Br+H2->HBr+H",
            "H+Br2->HBr+Br",
            "H+HBr->Br+H2",
            "2Br->Br2"
        );


        final int n2 = 15000;
        final double dt2 = 0.000001;
        final Double[] k2 = new Double[]{ 2.0, 1.0 };
        final Double[] r02 = { 15.5, 15.5, 0.0, 0.0 };
        final Stream<String> reaction2 = Stream.of(
            "2A+1B->4Q",
            "1A+4Q->5Z"
        );

        final List<com.nir.ui.dto.Stage> stages =
            reaction2
            .map(StageParser::parse)
            .map(StageParser::convert)
            .collect(Collectors.toList());

        final List<String> compounds = StehiomatrixGetter.getCompounds(stages);
        final Stehiomatrix matrix = StehiomatrixGetter.getMatrix(stages);
        final StageRates rates = StehiomatrixGetter.getRates(stages, k2);
        final F system = StehiomatrixGetter.times(matrix.transpose(), rates);

        new SolutionFlow(
            new InitialData(
                0.0,
                r02,
                dt2,
                n2
            ),
            system,
            EulerFlow.INSTANCE
        )
            .start(
                compounds, stage
            );
    }

    public static void main(String[] args) {
        launch();
    }
}
