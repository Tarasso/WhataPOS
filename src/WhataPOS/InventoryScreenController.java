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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.DoubleStringConverter;

import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.sql.*;

public class InventoryScreenController implements Initializable {

    @FXML private TableView inventoryTableView;

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

    public ObservableList<Dessert> getDesserts() {
        ObservableList<Dessert> desserts = FXCollections.observableArrayList();
        try {
            String sql = "select * from desserts";
            ResultSet rs = JDBC.execQuery(sql);
            while (rs.next()) {
                desserts.add(new Dessert(
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
        return desserts;
    }

    public ObservableList<Side> getSides() {
        ObservableList<Side> sides = FXCollections.observableArrayList();
        try {
            String sql = "select * from sides";
            ResultSet rs = JDBC.execQuery(sql);
            while (rs.next()) {
                sides.add(new Side(
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
        return sides;
    }

    public ObservableList<Entree> getEntrees() {
        ObservableList<Entree> entrees = FXCollections.observableArrayList();
        try {
            String sql = "select * from entrees";
            ResultSet rs = JDBC.execQuery(sql);
            while (rs.next()) {
                entrees.add(new Entree(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("availableQuantity"),
                        rs.getDouble("costToMake"),
                        rs.getDouble("salePrice"),
                        rs.getArray("toppings")
                ));
            }
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }
        return entrees;
    }

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

    public ObservableList<Entree> getRecEntrees() {
        ObservableList<Entree> recs = FXCollections.observableArrayList();
        try {
            String sql = "with \"orderInfo\" as (select unnest(\"order\") from order_data) select \"unnest\", count(\"unnest\") as \"MostCommon\" from \"orderInfo\" where \"unnest\" like 'E%' group by \"unnest\" order by \"MostCommon\" DESC LIMIT 3";
            ResultSet rs = JDBC.execQuery(sql);

            String[] id = new String[3];
            int i = 0;
            while (rs.next()) {
                id[i++] = rs.getString("unnest");
            }

            for (i = 0; i < id.length; ++i) {
                rs = JDBC.execQuery("select * from \"entrees\" where \"id\" = '" + id[i] + "'");
                while (rs.next()) {
                    recs.add(new Entree(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getInt("availableQuantity"),
                            rs.getDouble("costToMake"),
                            rs.getDouble("salePrice"),
                            rs.getArray("toppings")
                    ));
                }
            }
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }
        return recs;
    }

    public ObservableList<Beverage> getRecBeverages() {
        ObservableList<Beverage> recs = FXCollections.observableArrayList();
        try {
            String sql = "with \"orderInfo\" as (select unnest(\"order\") from order_data) select \"unnest\", count(\"unnest\") as \"MostCommon\" from \"orderInfo\" where \"unnest\" like 'B%' group by \"unnest\" order by \"MostCommon\" DESC LIMIT 3";
            ResultSet rs = JDBC.execQuery(sql);

            String[] id = new String[3];
            int i = 0;
            while (rs.next()) {
                id[i++] = rs.getString("unnest");
            }

            for (i = 0; i < id.length; ++i) {
                rs = JDBC.execQuery("select * from \"beverages\" where \"id\" = '" + id[i] + "'");
                while (rs.next()) {
                    recs.add(new Beverage(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getInt("availableQuantity"),
                                rs.getDouble("costToMake"),
                                rs.getDouble("salePrice")
                    ));
                }
            }
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }
        return recs;
    }

    public ObservableList<Dessert> getRecDesserts() {
        ObservableList<Dessert> recs = FXCollections.observableArrayList();
        try {
            String sql = "with \"orderInfo\" as (select unnest(\"order\") from order_data) select \"unnest\", count(\"unnest\") as \"MostCommon\" from \"orderInfo\" where \"unnest\" like 'D%' group by \"unnest\" order by \"MostCommon\" DESC LIMIT 3";
            ResultSet rs = JDBC.execQuery(sql);

            String[] id = new String[3];
            int i = 0;
            while (rs.next()) {
                id[i++] = rs.getString("unnest");
            }

            for (i = 0; i < id.length; ++i) {
                rs = JDBC.execQuery("select * from \"desserts\" where \"id\" = '" + id[i] + "'");
                while (rs.next()) {
                    recs.add(new Dessert(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getInt("availableQuantity"),
                            rs.getDouble("costToMake"),
                            rs.getDouble("salePrice")
                    ));
                }
            }
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }
        return recs;
    }

    @FXML
    public void actionShowBeverages(ActionEvent event) throws IOException {

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

    @FXML
    public void actionShowDesserts(ActionEvent event) throws IOException {

        TableColumn<Dessert, String> idColumn = new TableColumn<>("id");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        TableColumn<Dessert, String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Dessert, Integer> quantityColumn = new TableColumn<>("availableQuantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));

        TableColumn<Dessert, Double> costColumn = new TableColumn<>("costToMake");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));

        TableColumn<Dessert, Double> priceColumn = new TableColumn<>("salePrice");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        inventoryTableView.getColumns().clear();
        inventoryTableView.setItems(getDesserts());
        inventoryTableView.getColumns().addAll(
                idColumn,
                nameColumn,
                quantityColumn,
                costColumn,
                priceColumn);

    }

    @FXML
    public void actionShowSides(ActionEvent event) throws IOException {

        TableColumn<Side, String> idColumn = new TableColumn<>("id");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        TableColumn<Side, String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Side, Integer> quantityColumn = new TableColumn<>("availableQuantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));

        TableColumn<Side, Double> costColumn = new TableColumn<>("costToMake");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));

        TableColumn<Side, Double> priceColumn = new TableColumn<>("salePrice");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        inventoryTableView.getColumns().clear();
        inventoryTableView.setItems(getSides());
        inventoryTableView.getColumns().addAll(
                idColumn,
                nameColumn,
                quantityColumn,
                costColumn,
                priceColumn);

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

        TableColumn<Entree, Integer> quantityColumn = new TableColumn<>("availableQuantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        quantityColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Entree, Integer> t) -> {
                    Entree selectedEntree = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    selectedEntree.setAvailableQuantity(Integer.parseInt(t.getNewValue().toString()));
                    System.out.println(selectedEntree.getAvailableQuantity());
                }
        );
        quantityColumn.setCellFactory(TextFieldTableCell.<Entree, Integer>forTableColumn(new IntegerStringConverter()));

        TableColumn<Entree, Double> costColumn = new TableColumn<>("costToMake");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));

        TableColumn<Entree, Double> priceColumn = new TableColumn<>("salePrice");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        TableColumn<Entree, Array> toppingsColumn = new TableColumn<>("toppings");
        toppingsColumn.setCellValueFactory(new PropertyValueFactory<>("toppings"));

        inventoryTableView.getColumns().clear();
        inventoryTableView.setItems(getEntrees());
        inventoryTableView.getColumns().addAll(
                idColumn,
                nameColumn,
                typeColumn,
                quantityColumn,
                costColumn,
                priceColumn,
                toppingsColumn);

    }

    @FXML
    public void actionShowToppings(ActionEvent event) throws IOException {

        TableColumn<Topping, String> idColumn = new TableColumn<>("id");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Topping, String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Topping, Integer> quantityColumn = new TableColumn<>("availableQuantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));

        TableColumn<Topping, Double> costColumn = new TableColumn<>("costToMake");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));

        TableColumn<Topping, Double> priceColumn = new TableColumn<>("salePrice");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        inventoryTableView.getColumns().clear();
        inventoryTableView.setItems(getToppings());
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
        inventoryTableView.setEditable(true);
    }


}
