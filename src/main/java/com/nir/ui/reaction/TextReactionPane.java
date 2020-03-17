package com.nir.ui.reaction;

import com.nir.ui.utils.Height;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

class TextReactionPane extends ReactionPane {
    private final Button deleteEquationBtn;
    private GridPane gridPane;
    private AddReactionButton addEquationBtn;

    TextReactionPane(ReactionsPane reactionsPane) {
        super();
        gridPane = new GridPane();

        addEquationBtn = new AddReactionButton(reactionsPane);
        deleteEquationBtn = new DeleteReactionButton(reactionsPane, this);

        TextArea reactionText = new TextArea();
        int prefHeight = 50;
        addEquationBtn.setPrefWidth(prefHeight);
        deleteEquationBtn.setPrefWidth(prefHeight);
        reactionText.setPrefSize(500, prefHeight);
        reactionText.getStyleClass().add("reactionTextArea");

        gridPane.add(reactionText, 0, 0);
        gridPane.add(addEquationBtn, 1, 0);
        gridPane.add(deleteEquationBtn, 2, 0);

        Height.bind(addEquationBtn).to(this);
        Height.bind(deleteEquationBtn).to(this);

        this.prefWidthProperty().bind(reactionsPane.widthProperty());
        gridPane.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().addAll(gridPane);
    }
}
