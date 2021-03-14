package WhataPOS;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Vector;

public class SelectToppingSceneController implements Initializable {

    @FXML private TableView toppingsTableView;
    @FXML private TableView selectedToppingsTableView;
    @FXML private Button addToppingButton;
    @FXML private Button deleteToppingButton;
    @FXML private Button doneSelectButton;

    private Vector<Topping> toppings;

    public static void showAlert(Alert.AlertType alertType, Window owner, String message, String title) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initOwner(owner);
        alert.show();
    }

    public void setInitToppings(Vector<Topping> t) {
        for (Topping topping : t) {
            selectedToppingsTableView.getItems().add(topping);
        }
    }

    public int checkAvailableQuantity(String table, String id) throws SQLException {
        String SQL = "SELECT \"availableQuantity\" FROM " + table + " WHERE id = " + "'" + id + "'";
        ResultSet quantityRS = JDBC.execQuery(SQL);
        quantityRS.next();

        return quantityRS.getInt(1);
    }

    // Gathers data for toppings from database
    public ObservableList<Topping> getToppings() {
        ObservableList<Topping> toppings = FXCollections.observableArrayList();
        try {
            String sql = "select * from toppings";
            ResultSet rs = JDBC.execQuery(sql);
            while (rs.next()) {
                toppings.add(new Topping(
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
        return toppings;
    }

    @FXML
    public void actionSelectTopping(ActionEvent event) throws IOException, SQLException {
        var object = toppingsTableView.getSelectionModel().getSelectedItem();
        int quantity;

        Window toppingsTableOwner =  toppingsTableView.getScene().getWindow();

        if (Bindings.isEmpty(toppingsTableView.getSelectionModel().getSelectedItems()).get()) {
            showAlert(Alert.AlertType.ERROR, toppingsTableOwner, "No item is selected", "Error");
            return;
        }

        Topping selectedTopping = (Topping) object;

        quantity = checkAvailableQuantity("toppings", selectedTopping.getId());

        if (quantity <= 0) {
            showAlert(Alert.AlertType.ERROR, toppingsTableOwner, "Selected item is out of stock", "Error");
        } else {
            selectedToppingsTableView.getItems().add(selectedTopping);
        }
    }

    @FXML
    public void actionDeleteTopping(ActionEvent event) throws IOException {
        Window selectedToppingTableOwner = selectedToppingsTableView.getScene().getWindow();

        if (Bindings.isEmpty(selectedToppingsTableView.getItems()).get()) {
            showAlert(Alert.AlertType.ERROR, selectedToppingTableOwner, "Order is Empty", "Error");
            return;
        }

        var object = selectedToppingsTableView.getSelectionModel().getSelectedItem();

        Topping selectedTopping = (Topping) object;

        selectedToppingsTableView.getItems().remove(selectedTopping);
    }

    @FXML
    public void actionDoneSelectTopping(ActionEvent event) throws IOException {
        Stage stage = (Stage) toppingsTableView.getScene().getWindow();
        stage.close();
        toppings = new Vector(Arrays.asList(selectedToppingsTableView.getItems().toArray()));
    }

    public Vector<Topping> getSelectedToppings() {
        return toppings;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourcebundle) {

        // Set up the topping table view
        TableColumn<Topping, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(249);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Topping, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(60);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        toppingsTableView.getColumns().clear();
        toppingsTableView.setItems(getToppings());
        toppingsTableView.getColumns().addAll(nameColumn, priceColumn);

        // Set up the selected topping table view
        nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(90);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        selectedToppingsTableView.getColumns().clear();
        selectedToppingsTableView.getColumns().addAll(nameColumn, priceColumn);


    }

}
