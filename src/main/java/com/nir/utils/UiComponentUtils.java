package com.nir.utils;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class UiComponentUtils {
    public static void show(javafx.scene.Parent parent) {
        StackPane rootPane = new StackPane();
        rootPane.getChildren().addAll(parent);
        Scene mainScene = new Scene(rootPane);
        Stage mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.show();
    }
}
