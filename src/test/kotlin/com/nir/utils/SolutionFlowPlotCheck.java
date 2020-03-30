package com.nir.utils;

import com.nir.utils.math.flow.EulerFlow;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SolutionFlowPlotCheck extends Application {
    @Override
    public void start(Stage stage) {
        final List<String> titles = Stream.of("X", "Y", "Z").collect(arrayList());;

        new SolutionFlow(
                new InitialData(),
                new LorentzStrangeAttractor(),
                EulerFlow.INSTANCE
            )
            .start(
                titles, stage
            );
    }

    public static Collector<String, ?, ArrayList<String>> arrayList() {
        return Collectors.toCollection(ArrayList::new);
    }

    public static void main(String[] args) {
        launch();
    }
}
