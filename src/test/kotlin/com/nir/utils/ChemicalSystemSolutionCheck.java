package com.nir.utils;

import com.nir.beans.StageParser;
import com.nir.beans.StehiomatrixGetter;
import com.nir.utils.math.F;
import com.nir.utils.math.RungeKutta;
import com.nir.utils.math.StageRates;
import com.nir.utils.math.Stehiomatrix;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChemicalSystemSolutionCheck extends Application {
    @Override
    public void start(Stage stage) {

        final List<com.nir.ui.dto.Stage> stages =
            Stream.of(
                "Br2->2Br",
                "Br+H2->HBr+H",
                "H+Br2->HBr+Br",
                "H+HBr->Br+H2",
                "2Br->Br2"
            )
            .map(StageParser::parse)
            .map(StageParser::convert)
            .collect(Collectors.toList());

        final List<String> compounds = StehiomatrixGetter.getCompounds(stages);
        final Stehiomatrix matrix = StehiomatrixGetter.getMatrix(stages);
        final Double[] k = new Double[]{1.0, 1.0, 15.0, 1.0, 1.0};
        final StageRates rates = StehiomatrixGetter.getRates(stages, k);
        final F system = StehiomatrixGetter.times(matrix.transpose(), rates);

        new Solution(
            new InitialData(
                0.0,
                new Double[] { 5.5, 0.0, 12.0, 0.0, 50.0 },
                0.01,
                7000
            ),
            system,
            new RungeKutta(4)
        )
            .start(
                compounds, stage
            );
    }

    public static void main(String[] args) {
        launch();
    }
}
