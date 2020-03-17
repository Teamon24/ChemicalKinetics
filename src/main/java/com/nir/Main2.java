package com.nir;

import com.nir.ui.UiComponents;
import com.nir.ui.beans.Beans;
import com.nir.ui.reaction.ReactionsPane;
import com.nir.ui.utils.CSS;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main2 extends Application {

        public void start(Stage primaryStage) {
            StackPane rootPane = new StackPane();
            SplitPane splitPane = new SplitPane();
            ReactionsPane reactionsPane = new ReactionsPane();
            reactionsPane.setMinSize(700.0, 500.0);
            splitPane.getItems().addAll(reactionsPane, new TextArea("Empty Area"));
            rootPane.getChildren().addAll(splitPane);
            Scene scene = new Scene(rootPane);
            scene.getStylesheets().add(CSS.components());
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    public static void main(String[] args) {
        UiComponents.periodicTable();
        Beans.periodicElementsGetter();
        launch(args);
    }
}
