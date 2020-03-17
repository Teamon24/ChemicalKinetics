package com.nir.ui.reaction;

import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;

class DeleteReactionButton extends Button {
    DeleteReactionButton(ReactionsPane reactionsPane, ReactionPane reactionPane) {
        super("Delete");
        this.getStyleClass().addAll("button_DeleteEquation");
        this.setTextAlignment(TextAlignment.CENTER);
        this.setMinWidth(50);
        this.setOnMouseClicked(mouseEvent -> reactionsPane.removeReaction(reactionPane));
    }
}
