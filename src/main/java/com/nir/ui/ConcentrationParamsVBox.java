package com.nir.ui;

import com.nir.StageParser;
import com.nir.StehiomatrixGetter;
import com.nir.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConcentrationParamsVBox extends VBox {
    private ArrayList<TextField> values = new ArrayList<>();
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

    public ConcentrationParamsVBox(ArrayList<String> formulas) {
        Stream<String> stream = formulas.stream();
        final List<Stage> stages = stream.map(StageParser::parse).map(StageParser::convert).collect(Collectors.toList());
        final List<String> compounds = StehiomatrixGetter.getCompounds(stages);
        for (String compound : compounds) {
            HBox hBox = new HBox();
            Text text = new Text(compound);
            text.setWrappingWidth(80);
            hBox.getChildren().add(text);
            TextField textField = new TextField();
            textField.setPrefWidth(50);
            HBox.setMargin(textField, new Insets(8, 0, 4, 8));
            HBox.setMargin(text, new Insets(12, 0, 4, 16));
            textField.setTextFormatter(new TextFormatter<>(filter));
            values.add(textField);
            hBox.getChildren().add(textField);
            getChildren().add(hBox);
        }
    }

    public ArrayList<TextField> getValues() {
        return values;
    }

    public void setValues(ArrayList<TextField> vaues) {
        this.values = vaues;
    }
}
