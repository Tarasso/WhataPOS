package WhataPOS;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.sql.*;
import WhataPOS.JDBC;


public class InventoryScreenController implements Initializable {


    @FXML private TableView<Beverage> inventoryTableView;

    public ObservableList<Entree> getEntrees() {
        ObservableList<Entree> entrees = FXCollections.observableArrayList();
        //entrees.add(new Entree("1", "Whataburger", "Burger"));
        //entrees.add(new Entree("2", "Fishburger", "Fish"));
        return entrees;
    }

    public ObservableList<Beverage> getBeverages() {
        ObservableList<Beverage> beverages = FXCollections.observableArrayList();
        try {
            String sql = "select * from beverages";
            ResultSet rs = JDBC.execQuery(sql);
            while (rs.next()) {
                beverages.add(new Beverage(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getInt("availableQuantity"),
                            rs.getDouble("costToMake"),
                            rs.getDouble("salePrice")
                        ));
            }
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }
        return beverages;
    }

    @FXML
    public void actionShowEntrees(ActionEvent event) throws IOException {

        TableColumn<Beverage, String> idColumn = new TableColumn<>("id");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        TableColumn<Beverage, String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Beverage, Integer> quantityColumn = new TableColumn<>("availableQuantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));

        TableColumn<Beverage, Double> costColumn = new TableColumn<>("costToMake");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));

        TableColumn<Beverage, Double> priceColumn = new TableColumn<>("salePrice");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        inventoryTableView.getColumns().clear();
        inventoryTableView.setItems(getBeverages());
        inventoryTableView.getColumns().addAll(
                idColumn,
                nameColumn,
                quantityColumn,
                costColumn,
                priceColumn);

    }

    // Does not work with selected items
    @FXML
    public void actionDelete(ActionEvent event) throws IOException {
        ObservableList selectedEntries = inventoryTableView.getSelectionModel().getSelectedItems();
        inventoryTableView.getItems().remove(selectedEntries);
        inventoryTableView.getItems().clear();
    }

    public void actionLogOut(ActionEvent event) throws IOException {
        Parent welcomeScreenParent = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        Scene welcomeScreenScene = new Scene(welcomeScreenParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(welcomeScreenScene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventoryTableView.getColumns().clear();
        inventoryTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


}
