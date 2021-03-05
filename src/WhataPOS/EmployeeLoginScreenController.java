package WhataPOS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeLoginScreenController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;


//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//
//    }


    // Buttons
    public void actionReturnHome(ActionEvent event) throws IOException {
        Parent welcomeScreenParent = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        Scene welcomeScreenScene = new Scene(welcomeScreenParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(welcomeScreenScene);
        window.show();
    }

    public void actionLogin(ActionEvent event) {
        Window owner = txtUsername.getScene().getWindow();
        System.out.println(txtUsername.getText());
        System.out.println(txtPassword.getText());

        if (txtUsername.getText().isEmpty()) {

        }

        if (txtPassword.getText().isEmpty()) {

        }

    }
}
