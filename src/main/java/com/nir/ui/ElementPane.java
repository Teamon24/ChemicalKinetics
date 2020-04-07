package com.nir.ui;

import com.nir.ui.pojos.PeriodicElement;
import com.nir.ui.pojos.PeriodicElementType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class ElementPane extends GridPane {

    private List<Label> labels = new ArrayList<>();

    public ElementPane(PeriodicElement element) {
        super();
        int size = 30;
        this.setMinHeight(size + 20);

        Label number = new Label("" + element.getNumber());
        Label symbol = new Label(element.getSymbol());
        Label name = new Label(element.getName());
        Label atomicMass = new Label("" + element.getAtomicMass());

        labels.add(number);
        labels.add(symbol);
        labels.add(name);
        labels.add(atomicMass);

        symbol.setTextAlignment(TextAlignment.RIGHT);

        number.getStyleClass().add("number");
        symbol.getStyleClass().add("symbol");
        name.getStyleClass().add("name");
        atomicMass.getStyleClass().add("atomicMass");

        String type = element.getCategory();
        PeriodicElementType periodicElementType = PeriodicElementType.getBy(type);
        String classOfType = periodicElementType.getClassBy(type);
        this.getStyleClass().add(classOfType);

        this.setHgap(1);
        this.setVgap(1);
        this.setFocusTraversable(false);
        this.add(number, 0, 0);
        this.add(symbol, 0, 1);
        this.add(name, 0, 2);
        this.add(atomicMass, 0, 3);
    }

    public List<Label> getLables() {
        return this.labels;
    }
}
