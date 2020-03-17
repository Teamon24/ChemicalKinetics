package com.nir.ui.reaction;

import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ActionPane extends Pane {

    private TextArea textArea = new TextArea();
    private AddElementButton addElementButton = new AddElementButton();
    private NewCompoundButton newCompoundButton = new NewCompoundButton();
    private TypeReactionMenu typeReactionMenu = new TypeReactionMenu();

    private GridPane gridPane = new GridPane();

    public ActionPane() {
        gridPane.add(textArea, 0, 0);
        gridPane.add(addElementButton, 1, 0);
        gridPane.add(newCompoundButton, 2, 0);
        gridPane.add(typeReactionMenu, 3, 0);
        this.getChildren().add(gridPane);
    }
}
