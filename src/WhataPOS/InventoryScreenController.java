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
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Vector;


public class InventoryScreenController implements Initializable {


    @FXML private TableView<Entree> inventoryTableView;

    public ObservableList<Entree> getEntrees() {
        ObservableList<Entree> entrees = FXCollections.observableArrayList();
        entrees.add(new Entree("1", "Whataburger", "Burger", 20, 200.0, 200.0, new Vector<String>(Arrays.asList("Cheese", "Tomato"))));
        entrees.add(new Entree("2", "Fishburger", "Fish", 30, 300.0, 200.0, new Vector<String>(Arrays.asList("Cheese", "Tomato"))));
        return entrees;
    }

    @FXML
    public void actionShowEntrees(ActionEvent event) throws IOException {

        TableColumn<Entree, String> idColumn = new TableColumn<>("id");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        TableColumn<Entree, String> nameColumn = new TableColumn<>("name");
        nameColumn.setMinWidth(30);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableColumn<Entree, String> typeColumn = new TableColumn<>("type");
        typeColumn.setMinWidth(30);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));


        TableColumn<Entree, Integer> quantityColumn = new TableColumn<>("available quantity");
        quantityColumn.setMinWidth(30);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));


        TableColumn<Entree, Integer> costToMakeColumn = new TableColumn<>("cost to make");
        costToMakeColumn.setMinWidth(30);
        costToMakeColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));


        TableColumn<Entree, Double> salePriceColumn = new TableColumn<>("sale price");
        salePriceColumn.setMinWidth(30);
        salePriceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        TableColumn<Entree, Vector<String>> toppingsColumn = new TableColumn<>("toppings");
        toppingsColumn.setMinWidth(30);
        toppingsColumn.setCellValueFactory(new PropertyValueFactory<>("toppings"));


        inventoryTableView.getColumns().clear();
        inventoryTableView.setItems(getEntrees());
        inventoryTableView.getColumns().addAll(
                idColumn,
                nameColumn,
                typeColumn,
                quantityColumn,
                costToMakeColumn,
                salePriceColumn,
                toppingsColumn);

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
