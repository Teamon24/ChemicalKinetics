package com.nir.ui.reaction;

import com.nir.ui.utils.CSS;
import com.nir.ui.UiComponents;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Map;

public class PeriodicElementPicker extends Stage {
    private PeriodicTable periodicTable = UiComponents.periodicTable();
    private int pickedElementNumber = 0;

    public PeriodicElementPicker() {
        super();
        Scene scene = new Scene(periodicTable);
        scene.getStylesheets().add(CSS.elementsType());
        scene.getStylesheets().add(CSS.components());
        this.setScene(scene);
        Map<Integer, ElementPane> grid = periodicTable.getGrid();
        grid.entrySet().stream().forEach(numberAndElement -> {
            numberAndElement.getValue().setOnMouseClicked(mouseEvent -> {
                this.pickedElementNumber = numberAndElement.getKey();
                String template = "Picked element number = %s";
                System.out.println(String.format(template, this.pickedElementNumber));
                this.close();
            });
        });

    }

    public int getPickedElementNumber() {
        return pickedElementNumber;
    }
}
