package WhataPOS;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class OrderScreenController {

    // Element tag ids
    @FXML private ListView menuListView;
    @FXML private ListView orderListView;

    // Buttons
    @FXML
    public void actionWelcomeScreen(ActionEvent event) throws IOException {
        Parent welcomeScreenParent = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        Scene welcomeScreenScene = new Scene(welcomeScreenParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(welcomeScreenScene);
        window.show();
    }

    @FXML
    public void actionShowEntrees(ActionEvent event) throws IOException {
        menuListView.getItems().clear();
        menuListView.getItems().addAll("Whataburger 1", "Whataburger 2", "Whataburger 3");
        menuListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void actionShowBeverages(ActionEvent event) throws IOException {
        menuListView.getItems().clear();
        menuListView.getItems().addAll("Beverage 1", "Beverage 2", "Beverage 3");
        menuListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void actionShowDesserts(ActionEvent event) throws IOException {
        menuListView.getItems().clear();
        menuListView.getItems().addAll("Dessert 1", "Dessert 2", "Dessert 3");
        menuListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void actionShowSides(ActionEvent event) throws IOException {
        menuListView.getItems().clear();
        menuListView.getItems().addAll("Side 1", "Side 2", "Side 3");
        menuListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void actionShowToppings(ActionEvent event) throws IOException {
        menuListView.getItems().clear();
        menuListView.getItems().addAll("Topping 1", "Topping 2", "Topping 3");
        menuListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void actionSelectItem(ActionEvent event) throws IOException {
        ObservableList listOfItems = menuListView.getSelectionModel().getSelectedItems();
        orderListView.getItems().addAll(listOfItems);
    }


}
