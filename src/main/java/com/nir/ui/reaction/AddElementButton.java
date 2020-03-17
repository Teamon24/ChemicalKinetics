package com.nir.ui.reaction;

import com.nir.ui.UiComponents;
import javafx.scene.control.Button;

class AddElementButton extends Button {

    private PeriodicElementPicker periodicElementPicker = UiComponents.periodicElementPicker();

    AddElementButton() {
        super("+");
        this.setPrefSize(50, 50);
        this.setOnAction(event -> {
            this.periodicElementPicker.setTitle("Choose Element");
            this.periodicElementPicker.show();
            this.periodicElementPicker.setOnCloseRequest(windowEvent -> this.periodicElementPicker = UiComponents.periodicElementPicker());
        });
    }
}
