package com.nir.ui.reaction;

import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;

class AddReactionButton extends Button {
    AddReactionButton(ReactionsPane reactionsPane) {
        super("Add");
        this.setTextAlignment(TextAlignment.CENTER);
        this.getStyleClass().addAll("button_AddEquation");
        this.setOnMouseClicked(ignored -> reactionsPane.addReaction());
    }
}
