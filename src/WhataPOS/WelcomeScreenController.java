package WhataPOS;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeScreenController {

    public void loginButtonClicked() {
        System.out.println("User logged in...");
    }

    // Buttons

    public void changeOrderScreenPushed(ActionEvent event) throws IOException {
        Parent orderScreenParent = FXMLLoader.load(getClass().getResource("OrderScreen.fxml"));
        Scene orderScreenScene = new Scene(orderScreenParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(orderScreenScene);
        window.show();
    }
    public void changeEmployeeLoginScreenPushed(ActionEvent event) throws IOException {
        Parent employeeLoginScreenParent = FXMLLoader.load(getClass().getResource("InventoryScreen.fxml"));
        Scene employeeLoginScreenScene = new Scene(employeeLoginScreenParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(employeeLoginScreenScene);
        window.show();
    }






}
