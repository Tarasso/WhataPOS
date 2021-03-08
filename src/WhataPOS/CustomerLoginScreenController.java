package WhataPOS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerLoginScreenController implements Initializable {

    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourcebundle) {

    }

    // Standard alert box for different uses
    public static void showAlert(Alert.AlertType alertType, Window owner, String message, String title) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initOwner(owner);
        alert.show();
    }

    public void actionOrderScreen(ActionEvent event) throws IOException {
        Window fnWindowOwner = firstNameTextField.getScene().getWindow();
        Window lnWindowOwner = lastNameTextField.getScene().getWindow();

        if (firstNameTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, fnWindowOwner, "First name is empty UwU", "Form error!");
            return;
        }

        if (lastNameTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, lnWindowOwner, "Last name is empty UwU", "Form error!");
            return;
        }

        Parent orderScreenParent = FXMLLoader.load(getClass().getResource("OrderScreen.fxml"));
        Scene orderScreenScene = new Scene(orderScreenParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(orderScreenScene);
        window.show();

    }



    public void actionWelcomeScreen(ActionEvent event) throws IOException {
        Parent welcomeScreenParent = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        Scene welcomeScreenScene = new Scene(welcomeScreenParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(welcomeScreenScene);
        window.show();
    }
}
