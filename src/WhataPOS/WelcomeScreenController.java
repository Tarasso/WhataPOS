package WhataPOS;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeScreenController {

    public void actionCustomerLogin(ActionEvent event) throws IOException {
        Parent customerLoginParent = FXMLLoader.load(getClass().getResource("CustomerLoginScreen.fxml"));
        Scene customerLoginScene = new Scene(customerLoginParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(customerLoginScene);
        window.show();
    }
    public void actionEmployeeLogin(ActionEvent event) throws IOException {
        Parent employeeLoginScreenParent = FXMLLoader.load(getClass().getResource("EmployeeLoginScreen.fxml"));
        Scene employeeLoginScreenScene = new Scene(employeeLoginScreenParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(employeeLoginScreenScene);
        window.show();
    }






}
