package com.nir;

import com.nir.ui.ConcentrationParamsVBox;
import com.nir.ui.EquationParamsVBox;
import com.nir.utils.*;
import de.gsi.chart.XYChart;
import de.gsi.dataset.spi.DoubleDataSet;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main2 extends Application {
    private Scene chooseReactionScene;
    private Scene addReactionScene;
    private Stage mainStage;
    private Stage addReactionStage;
    private Stage chooseReactionStage;

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

    private EquationParamsVBox equationParamsVBox;
    private ConcentrationParamsVBox concentrationParamsVBox;
    private ModelingParams reaction;
    private TextField tfN;
    private TextField tfDt;
    private ArrayList<String> elEqs;

    public void start(Stage primaryStage) {
        chooseReactionStage = primaryStage;
        try {
            chooseReactionScene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/choose_reaction.fxml")));
            primaryStage.setScene(chooseReactionScene);
            primaryStage.show();
            onShow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Text labelEq;
    private Text labelC;
    private DBHelper dbhelper = new DBHelper();
    private ChangeListener listener = (observableValue, o, t1) -> {
        VBox eqVBox = (VBox) chooseReactionScene.lookup("#eqBox");
        VBox cBox = (VBox) chooseReactionScene.lookup("#cBox");
        eqVBox.getChildren().clear();
        cBox.getChildren().clear();
        try {
            long id;
            if (t1 == null) {
                id = ((ComboBoxItem) o).getId();
            } else {
                id = ((ComboBoxItem) t1).getId();
            }
            elEqs = dbhelper.getReactions(id);
            equationParamsVBox = new EquationParamsVBox(elEqs);
            concentrationParamsVBox = new ConcentrationParamsVBox(elEqs);
            eqVBox.getChildren().add(equationParamsVBox);
            cBox.getChildren().add(concentrationParamsVBox);
            labelEq.setVisible(true);
            labelC.setVisible(true);
            chooseReactionStage.sizeToScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    private void onShow() {
        Button btnAddNew = (Button) chooseReactionScene.lookup("#btnAddNew");
        btnAddNew.setOnMouseClicked(mouseEvent -> showAddReactionForm());
        Button btnOK = (Button) chooseReactionScene.lookup("#btnOK");
        tfN = (TextField) chooseReactionScene.lookup("#tfN");
        tfDt = (TextField) chooseReactionScene.lookup("#tfDt");
        tfN.setTextFormatter(new TextFormatter<>(filter));
        tfDt.setTextFormatter(new TextFormatter<>(filter));
        btnOK.setOnMouseClicked(mouseEvent -> {
            reaction = buildReaction();
            if (reaction != null) {
                showMainForm(reaction);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(null);
                alert.setHeaderText("Ошибка");
                alert.setContentText("Не все поля заполнены");
                alert.showAndWait();
            }
        });
        ComboBox comboBox = (ComboBox) chooseReactionScene.lookup("#comboBox");
        try {
            labelEq = (Text) chooseReactionScene.lookup("#labelEq");
            labelC = (Text) chooseReactionScene.lookup("#labelC");
            ObservableList<ComboBoxItem> options = FXCollections.observableArrayList(dbhelper.getComplexReactions());
            comboBox.setItems(options);
            comboBox.valueProperty().addListener(listener);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private ModelingParams buildReaction() {
        double dt;
        int n;
        Double[] c0;
        Double[] k;
        try {
            dt = Double.parseDouble(tfDt.getText());
            n = Integer.parseInt(tfN.getText());
            ArrayList<TextField> values = concentrationParamsVBox.getValues();
            c0 = new Double[values.size()];
            for (int i = 0; i < values.size(); i++) {
                try {
                    c0[i] = Double.parseDouble(values.get(i).getText());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            ArrayList<TextField> kVals = equationParamsVBox.getK0Values();
            k = new Double[kVals.size()];
            for (int i = 0; i < kVals.size(); i++) {
                try {
                    k[i] = Double.parseDouble(kVals.get(i).getText());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return new ModelingParams(n, 0, dt, k, c0, elEqs.stream());
    }

    public Stage getMainStage() {
        return mainStage;
    }

    private void showMainForm(ModelingParams modelingParams) {
        try {
            mainStage = new Stage();
            chooseReactionStage.hide();
            mainStage.setOnHiding(windowEvent -> chooseReactionStage.show());
            mainStage.setMaximized(true);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            mainStage.setX(bounds.getMinX());
            mainStage.setY(bounds.getMinY());
            mainStage.setWidth(bounds.getWidth());
            mainStage.setHeight(bounds.getHeight());

            final Stream<String> reaction = modelingParams.getReaction();
            final List<com.nir.Stage> stages =
                    reaction
                            .map(StageParser::parse)
                            .map(StageParser::convert)
                            .collect(Collectors.toList());
            //Подготовка объектов с данными решения системы
            //и открытие пустых графиков
            final List<String> compounds = StehiomatrixGetter.getCompounds(stages);
            final List<DoubleDataSet> dataSets = PlotUtils.dataSets(compounds);
            final List<XYChart> charts = PlotUtils.charts(dataSets);
            PlotUtils.show(charts, this);
            //Составление объекта с настройками решения
            final InitialData initialData = modelingParams.initialData();
            final Method method = new RungeKutta(4, initialData);
            final MySystem mySystem = getSystem(modelingParams, stages);
            final SolutionFlow solutionFlow =
                    Solution
                            .method(method)
                            .system(mySystem)
                            .initialData(initialData)
                            .datasets(dataSets)
                            .flow();
            //Запуск решения системы уравнений - на этом моменте времене в графики будут добавляться точки решения
            PlatformUtils.runLater(solutionFlow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private MySystem getSystem(ModelingParams modelingParams, List<com.nir.Stage> stages) {
        final Stehiomatrix matrix = StehiomatrixGetter.getMatrix(stages);
        Double[] k = modelingParams.getK();
        final StageRates rates = StehiomatrixGetter.getRates(stages, k);
        return StehiomatrixGetter.times(matrix.transpose(), rates);
    }



    private void showAddReactionForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/add_reaction.fxml"));
            addReactionScene = new Scene(fxmlLoader.load());
            addReactionStage = new Stage();
            addReactionStage.setScene(addReactionScene);
            addReactionStage.show();

            Button btnCancel = (Button) addReactionScene.lookup("#btnCancel");
            Button btnAdd = (Button) addReactionScene.lookup("#btnAdd");
            TextField tfReaction = (TextField) addReactionScene.lookup("#tfReaction");
            VBox reactionsBox = (VBox) addReactionScene.lookup("#reactionsBox");
            btnAdd.setOnMouseClicked(mouseEvent -> {
                TextField textField = new TextField();
                reactionsBox.getChildren().add(textField);
                VBox.setMargin(textField, new Insets(8, 0, 8, 0));
                addReactionStage.sizeToScene();
            });
            Button btnSave = (Button) addReactionScene.lookup("#btnSave");
            btnSave.setOnMouseClicked(mouseEvent1 -> {
                ObservableList<Node> children = reactionsBox.getChildren();
                try {
                    dbhelper.insertReaction(tfReaction.getText(), children);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                addReactionStage.close();
            });
            btnCancel.setOnMouseClicked(mouseEvent1 -> addReactionStage.close());
            addReactionStage.setOnHiding(windowEvent -> chooseReactionStage.show());
            chooseReactionStage.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
