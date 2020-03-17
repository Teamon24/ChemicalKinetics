package com.nir.ui.reaction;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ReactionBuilder extends Pane {
    private AddElementButton addElement;
    private GridPane gridPane = new GridPane();

    public ReactionBuilder() {
        this.addElement = new AddElementButton();
        this.gridPane.add(addElement, 0, 0);
        this.getChildren().add(gridPane);
    }
}
