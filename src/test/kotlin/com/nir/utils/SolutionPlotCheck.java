package com.nir.utils;

import com.nir.utils.math.RungeKutta;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SolutionPlotCheck extends Application {
    @Override
    public void start(Stage stage) {
        final ArrayList<String> titles = Stream.of("X", "Y", "Z").collect(Collectors.toCollection(ArrayList::new));

        new Solution(
                new InitialData(),
                new LorentzStrangeAttractor(),
                new RungeKutta(4)
            )
            .start(
                titles, stage
            );
    }

    public static void main(String[] args) {
        launch();
    }
}
