package com.nir.ui.reaction;

import com.nir.ui.utils.Height;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class UiReactionPane extends ReactionPane {
    private final Button deleteEquationBtn;
    private GridPane gridPane;
    private AddReactionButton addEquationBtn;

    UiReactionPane(ReactionsPane reactionsPane) {
        super();
        gridPane = new GridPane();

        addEquationBtn = new AddReactionButton(reactionsPane);
        deleteEquationBtn = new DeleteReactionButton(reactionsPane, this);

        ReactionBuilder child = new ReactionBuilder();
        gridPane.add(child, 0, 0);
        gridPane.add(addEquationBtn, 1, 0);
        gridPane.add(deleteEquationBtn, 2, 0);

        Height.bind(addEquationBtn).to(this);
        Height.bind(deleteEquationBtn).to(this);

        this.prefWidthProperty().bind(reactionsPane.widthProperty());
        gridPane.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().addAll(gridPane);
    }
}
