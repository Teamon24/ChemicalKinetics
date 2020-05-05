package com.nir.ui;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class EquationParamsVBox extends VBox {
    private UnaryOperator<TextFormatter.Change> filter = t -> {
        if (t.isReplaced())
            if (t.getText().matches("[^0-9]"))
                t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));
        if (t.isAdded()) {
            if (t.getControlText().contains(".")) {
                if (t.getText().matches("[^0-9]")) {
                    t.setText("");
                }
            } else if (t.getText().matches("[^0-9.]")) {
                t.setText("");
            }
        }
        return t;
    };

    private ArrayList<TextField> k0Values = new ArrayList<>();

    public ArrayList<TextField> getK0Values() {
        return k0Values;
    }

    public void setK0Values(ArrayList<TextField> k0Values) {
        this.k0Values = k0Values;
    }

    public ArrayList<TextField> getR0Values() {
        return r0Values;
    }

    public void setR0Values(ArrayList<TextField> r0Values) {
        this.r0Values = r0Values;
    }

    private ArrayList<TextField> r0Values = new ArrayList<>();

    public EquationParamsVBox(ArrayList<String> formulas) {
        for (int i = 0; i < formulas.size(); i++) {
            Text text = new Text(String.format("%d) %s", (i + 1), formulas.get(i)));
            VBox.setMargin(text, new Insets(8, 0, 8, 16));
            getChildren().add(text);
            HBox k0Box = new HBox();
            Text k0 = new Text("k0 =");
            k0Box.getChildren().add(k0);
            TextField textField = new TextField();
            textField.setPrefWidth(50);
            textField.setTextFormatter(new TextFormatter<>(filter));
            k0Values.add(textField);
            HBox.setMargin(textField, new Insets(0, 0, 4, 8));
            HBox.setMargin(k0, new Insets(4, 0, 4, 16));
            k0Box.getChildren().add(textField);
            getChildren().add(k0Box);
        }
    }
}
