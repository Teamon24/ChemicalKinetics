package com.nir;

import com.mysql.cj.jdbc.Driver;
import com.nir.ui.UiComponents;
import com.nir.ui.beans.Beans;
import com.nir.ui.beans.RawReaction;
import com.nir.ui.beans.ReactionParser;
import com.nir.ui.utils.CSS;
import com.nir.ui.utils.Reaction;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.awt.*;
import java.sql.DriverManager;
import java.util.ArrayList;

public class Main2 extends Application {
    private Scene chooseReactionScene;
    private Scene addReactionScene;
    private Scene mainScene;
    private Stage mainStage;
    private Stage addReactionStage;
    private Stage chooseReactionStage;

    public void start(Stage primaryStage) {
        chooseReactionStage = primaryStage;
        try {
            chooseReactionScene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/choose_reaction.fxml")));
            chooseReactionScene.getStylesheets().add(CSS.components());
            primaryStage.setScene(chooseReactionScene);
            primaryStage.show();

            Button btnAddNew = (Button) chooseReactionScene.lookup("#btnAddNew");
            btnAddNew.setOnMouseClicked(mouseEvent -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/add_reaction.fxml"));
                    addReactionScene = new Scene(fxmlLoader.load());
                    addReactionStage = new Stage();
                    addReactionStage.setScene(addReactionScene);
                    addReactionStage.show();
                    Button btnSave = (Button) addReactionScene.lookup("#btnSave");
                    btnSave.setOnMouseClicked(mouseEvent1 -> {
                        //update reactions list in chooseReactionScene
                        addReactionStage.close();

                    });
                    Button btnCancel = (Button) addReactionScene.lookup("#btnCancel");
                    btnCancel.setOnMouseClicked(mouseEvent1 -> {
                        addReactionStage.close();

                    });
                    addReactionStage.setOnHiding(windowEvent -> {
                        chooseReactionStage.show();
                    });
                    chooseReactionStage.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            Button btnOK = (Button) chooseReactionScene.lookup("#btnOK");
            btnOK.setOnMouseClicked(mouseEvent -> {
                ArrayList<Reaction> reactions = new ArrayList<>();
                ComboBox comboBox = (ComboBox) chooseReactionScene.lookup("#comboBox");
                String reactionString = (String) comboBox.getValue();
                //connect to database
                //search reaction in db, get id of complex reaction, find reactions with foreign key
                ReactionParser parser = ReactionParser.INSTANCE;
                //for...{
                    RawReaction rawReaction = parser.parse(reactionString);
                    Reaction reaction = parser.convert(rawReaction);
                    reactions.add(reaction);
                // }
                // составляем стехиометрическую матрицу, систему, решаем, получаем результат, открываем мейн форму, отображаем результат
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main_form.fxml"));
                    mainScene = new Scene(fxmlLoader.load());
                    mainStage = new Stage();
                    mainStage.setScene(mainScene);
                    mainStage.show();
                    chooseReactionStage.hide();
                    mainStage.setOnHiding(windowEvent -> {
                        chooseReactionStage.show();
                    });
                    MenuBar menuBar = (MenuBar) mainScene.lookup("#menuBar");
                    menuBar.getMenus().get(0).getItems().get(0).setOnAction(actionEvent -> {
                        mainStage.close();
                        chooseReactionStage.show();
                    });
                    menuBar.getMenus().get(0).getItems().get(1).setOnAction(actionEvent -> {
                        System.exit(0);
                    });
                    menuBar.getMenus().get(0).getItems().get(2).setOnAction(actionEvent -> {
                        System.exit(0);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        StackPane rootPane = new StackPane();
//        SplitPane splitPane = new SplitPane();
//        ReactionsPane reactionsPane = new ReactionsPane();
//        reactionsPane.setMinSize(700.0, 500.0);
//        splitPane.getItems().addAll(reactionsPane, new TextArea("Empty Area"));
//        rootPane.getChildren().addAll(splitPane);

    }

    public static void main(String[] args) {
        UiComponents.periodicTable();
        Beans.periodicElementsGetter();
        launch(args);
    }
}
