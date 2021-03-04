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

    public void changeOrderScreenPushed(ActionEvent event) throws IOException {
        Parent orderScreenParent = FXMLLoader.load(getClass().getResource("OrderScreen.fxml"));
        Scene orderScreenScene = new Scene(orderScreenParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(orderScreenScene);
        window.show();

    }

}
