package com.nir.ui.reaction;


import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class TypeReactionMenu extends MenuButton {

    public TypeReactionMenu() {
        super("->");

        MenuItem menuItem1 = new MenuItem("->");
        MenuItem menuItem2 = new MenuItem("<->");
        MenuItem menuItem3 = new MenuItem("=>");
        MenuItem menuItem4 = new MenuItem("<=>");

        this.getItems().add(menuItem1);
        this.getItems().add(menuItem2);
        this.getItems().add(menuItem3);
        this.getItems().add(menuItem4);
    }
}
