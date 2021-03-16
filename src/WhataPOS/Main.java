package WhataPOS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;
import WhataPOS.JDBC;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        primaryStage.setTitle("WhataPOS");
        primaryStage.setScene(new Scene(root, 1024, 576));
        primaryStage.show();
    }


    public static void main(String[] args) {
//        JDBC jdbc = new JDBC(
//            "jdbc:postgresql://47.186.232.151:63333/db901_group9_project2",
//            "kyle_m",
//            "Tarasso"
//        );

//        JDBC jdbc = new JDBC(
//            "jdbc:postgresql://47.186.232.151:63333/db901_group9_project2",
//            "liam_h",
//            "cosmicequation"
//        );

        JDBC jdbc = new JDBC(
            "jdbc:postgresql://csce-315-db.engr.tamu.edu/db901_group9_project2",
            "kylelamb1324",
            "426005497"
        );

        jdbc.newConnection();
        launch(args);
        jdbc.closeConnection();
    }
}
