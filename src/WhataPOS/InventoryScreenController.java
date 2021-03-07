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


public class InventoryScreenController implements Initializable {


    @FXML private TableView<Entree> inventoryTableView;

    public ObservableList<Entree> getEntrees() {
        ObservableList<Entree> entrees = FXCollections.observableArrayList();
        //entrees.add(new Entree("1", "Whataburger", "Burger"));
        //entrees.add(new Entree("2", "Fishburger", "Fish"));
        return entrees;
    }

    @FXML
    public void actionShowEntrees(ActionEvent event) throws IOException {

        TableColumn<Entree, String> idColumn = new TableColumn<>("id");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        TableColumn<Entree, String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableColumn<Entree, String> typeColumn = new TableColumn<>("type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        inventoryTableView.getColumns().clear();
        inventoryTableView.setItems(getEntrees());
        inventoryTableView.getColumns().addAll(
                idColumn,
                nameColumn,
                typeColumn);

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
