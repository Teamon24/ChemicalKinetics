package com.nir;

import com.nir.ui.UiComponents;
import com.nir.beans.Beans;
import com.nir.beans.RawStage;
import com.nir.beans.StageParser;
import com.nir.utils.CSS;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

import java.util.ArrayList;
import javafx.stage.Stage;

public class Main2 extends Application {
    private Scene chooseReactionScene;
    private Scene addReactionScene;
    private Scene mainScene;
    private Stage mainStage;
    private Stage addStageStage;
    private Stage chooseReactionStage;

    public static final EventHandler<ActionEvent> EXIT = ignored -> System.exit(0);

    public void start(Stage primaryStage) {
        chooseReactionStage = primaryStage;
        try {
            chooseReactionScene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/choose_reaction.fxml")));
            chooseReactionScene.getStylesheets().add(CSS.components());
            primaryStage.setScene(chooseReactionScene);
            primaryStage.show();

            Button btnAddNew = (Button) chooseReactionScene.lookup("#btnAddNew");
            btnAddNew.setOnMouseClicked(mouseEvent -> showAddReactionForm());
            Button btnOK = (Button) chooseReactionScene.lookup("#btnOK");
            btnOK.setOnMouseClicked(mouseEvent -> {
                ArrayList<com.nir.ui.pojos.ReactionStage> reactionStages = new ArrayList<>();
                ComboBox comboBox = (ComboBox) chooseReactionScene.lookup("#comboBox");
                String reactionString = (String) comboBox.getValue();
                //connect to database
                //search reaction in db, get id of complex reaction, find reactions with foreign key
                //for...{
                    RawStage rawReaction = StageParser.parse(reactionString);
                    com.nir.ui.pojos.ReactionStage reactionStage = StageParser.convert(rawReaction);
                    reactionStages.add(reactionStage);
                // }
                // составляем стехиометрическую матрицу, систему, решаем, получаем результат, открываем мейн форму, отображаем результат
                showMainForm();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
     }

    private void showMainForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main_form.fxml"));
            mainScene = new Scene(fxmlLoader.load());
            mainStage = new Stage();
            mainStage.setScene(mainScene);
            mainStage.show();
            chooseReactionStage.hide();
            mainStage.setOnHiding(windowEvent -> chooseReactionStage.show());

            MenuBar menuBar = (MenuBar) mainScene.lookup("#menuBar");
            final Menu submenu = menuBar.getMenus().get(0);
            submenu.getItems().get(0).setOnAction(actionEvent -> {
                mainStage.close();
                chooseReactionStage.show();
            });
            submenu.getItems().get(1).setOnAction(EXIT);
            submenu.getItems().get(2).setOnAction(EXIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddReactionForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/add_reaction.fxml"));
            addReactionScene = new Scene(fxmlLoader.load());
            addStageStage = new Stage();
            addStageStage.setScene(addReactionScene);
            addStageStage.show();
            Button btnSave = (Button) addReactionScene.lookup("#btnSave");
            btnSave.setOnMouseClicked(mouseEvent1 -> {
                //update reactions list in chooseReactionScene
                addStageStage.close();
            });
            Button btnCancel = (Button) addReactionScene.lookup("#btnCancel");
            btnCancel.setOnMouseClicked(mouseEvent1 -> addStageStage.close());
            addStageStage.setOnHiding(windowEvent -> chooseReactionStage.show());
            chooseReactionStage.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UiComponents.periodicTable();
        Beans.periodicElementsGetter();
        launch(args);
    }
}
