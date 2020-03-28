package com.nir.ui.utils;

import com.nir.ui.reaction.ReactionsPane;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class UiComponentUtils {
    public static void show(javafx.scene.Parent parent) {
        StackPane rootPane = new StackPane();
        ReactionsPane reactionsPane = new ReactionsPane();
        reactionsPane.setMinSize(700.0, 500.0);
        rootPane.getChildren().addAll(parent);
        Scene mainScene = new Scene(rootPane);
        Stage mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.show();
    }
}
