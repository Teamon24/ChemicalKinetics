package com.nir.ui.reaction;

import com.nir.ui.utils.Width;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ReactionsPane extends Pane {
    private int columnCounter = 0;
    private final GridPane mainGrid;
    private final Pane buttonPane;
    private final ScrollPane scrollPane;
    private GridPane equationsGrid;

    public ReactionsPane() {
        super();

        mainGrid = new GridPane();
        scrollPane = new ScrollPane();
        equationsGrid = new GridPane();

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(equationsGrid);

        buttonPane = getButtonPane();
        mainGrid.add(buttonPane, 0, 0);
        mainGrid.add(scrollPane, 0, 1, 1, 1);


        scrollPane.prefHeightProperty().bind(this.heightProperty());
        equationsGrid.minHeightProperty().bind(scrollPane.heightProperty());

        Width.bind(mainGrid).to(this);
        Width.bind(scrollPane).to(this);
        Width.bind(equationsGrid).to(scrollPane);

        this.getChildren().add(mainGrid);
    }

    private Pane getButtonPane() {
        AddReactionButton add = new AddReactionButton(this);
        Button deleteLast = new Button("Delete Last");
        deleteLast.setOnMouseClicked(mouseEvent -> {
            ObservableList<Node> children = this.equationsGrid.getChildren();
            if (!children.isEmpty()) {
                children.remove(children.size() - 1);
            }
        });

        Pane buttonPane = new Pane();
        GridPane buttonGrid = new GridPane();

        buttonGrid.add(add, 0, 0);
        buttonGrid.add(deleteLast, 1, 0);

        buttonPane.getChildren().addAll(buttonGrid);
        return buttonPane;
    }

    void addReaction() {
        ReactionPane reactionPane = new TextReactionPane(this);
        reactionPane.prefWidthProperty().bind(equationsGrid.prefWidthProperty());
        this.equationsGrid.add(reactionPane, 0, columnCounter++);
    }

    void removeReaction(ReactionPane e) {
        this.equationsGrid.getChildren().remove(e);
    }
}
