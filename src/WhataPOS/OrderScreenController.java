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
    @FXML private TableView menuTableView;
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

    }

    @FXML
    public void actionShowBeverages(ActionEvent event) throws IOException {
    }

    @FXML
    public void actionShowDesserts(ActionEvent event) throws IOException {
    }

    @FXML
    public void actionShowSides(ActionEvent event) throws IOException {
    }

    @FXML
    public void actionShowToppings(ActionEvent event) throws IOException {
    }

    @FXML
    public void actionSelectItem(ActionEvent event) throws IOException {
    }


}
