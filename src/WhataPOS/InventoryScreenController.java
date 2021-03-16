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
import javafx.scene.control.*;
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
import java.time.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class InventoryScreenController implements Initializable {

    @FXML private TableView inventoryTableView;
    @FXML private Button topChoicesButton;
    @FXML private DatePicker beginDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private boolean trendingUp;


    public void actionTrendingUp() throws IOException {
        trendingUp = true;
    }

    public void actionTrendingDown() throws IOException{
        trendingUp = false;
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

    public ObservableList<Entree> getRecEntrees(boolean getTrendingUp) {
        ObservableList<Entree> recs = FXCollections.observableArrayList();
        try {
            LocalDate date_lower = beginDatePicker.getValue(); // get from gui
            LocalDate date_upper = endDatePicker.getValue(); // get from gui
            String sql = "select \"date\", \"order\" from order_data";
            ResultSet rs = JDBC.execQuery(sql);

            Map<String, Integer> occurrences = new HashMap<String, Integer>();
            Map<String, String[]> json;

            Entry<String, Integer> min = null;
            Entry<String, Integer> max = null;

            Gson gson = new Gson();
            Type entMapType = new TypeToken<Map<String, String[]>>() {}.getType();

            Entry<String, Integer> test = null;
            while (rs.next()) {
                // date object
                LocalDate date_db = rs.getDate("date").toLocalDate();

                // check if date in range
                if (date_db.compareTo(date_upper) > 0) // greater than upper bound, exit
                    break;
                else if (date_db.compareTo(date_lower) < 0) // less than lower bound, continue
                    continue;

                // convert json to Map<String, String[]>
                json = gson.fromJson(rs.getString("order"), entMapType);

                // count occurrences of item IDs discard customization suffix
                for (String k : json.keySet()) {
                    k = k.split("_")[0];
                    if (k.charAt(0) != 'E')
                        continue;
                    if (occurrences.containsKey(k))
                        occurrences.put(k, occurrences.get(k) + 1);
                    else
                        occurrences.put(k, 1);
                }

                // find min and max occurrences
                for (Entry<String, Integer> entry : occurrences.entrySet()) {
                    if (min == null || min.getValue() > entry.getValue())
                        min = entry;
                    if (max == null || max.getValue() < entry.getValue())
                        max = entry;
                }

                // return min or max based on boolean parameter
                if (getTrendingUp)
                    test = max;
                else
                    test = min;
            }
            // perform lookup on test key and get row from table
            rs = JDBC.execQuery("select * from \"entrees\" where \"id\" = '" + test.getKey() + "'");
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
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }
        return recs;
    }

    public ObservableList<Beverage> getRecBeverages(boolean getTrendingUp) {
        ObservableList<Beverage> recs = FXCollections.observableArrayList();
        try {
            LocalDate date_lower = beginDatePicker.getValue(); // get from gui
            LocalDate date_upper = endDatePicker.getValue(); // get from gui
            String sql = "select \"date\", \"order\" from order_data";
            ResultSet rs = JDBC.execQuery(sql);

            Map<String, Integer> occurrences = new HashMap<String, Integer>();
            Map<String, String[]> json;

            Entry<String, Integer> min = null;
            Entry<String, Integer> max = null;

            Gson gson = new Gson();
            Type entMapType = new TypeToken<Map<String, String[]>>() {}.getType();

            Entry<String, Integer> test = null;
            while (rs.next()) {
                // date object
                LocalDate date_db = rs.getDate("date").toLocalDate();

                // check if date in range
                if (date_db.compareTo(date_upper) > 0) // greater than upper bound, exit
                    break;
                else if (date_db.compareTo(date_lower) < 0) // less than lower bound, continue
                    continue;

                // convert json to Map<String, String[]>
                json = gson.fromJson(rs.getString("order"), entMapType);

                // count occurrences of item IDs discard customization suffix
                for (String k : json.keySet()) {
                    k = k.split("_")[0];
                    if (k.charAt(0) != 'B')
                        continue;
                    if (occurrences.containsKey(k))
                        occurrences.put(k, occurrences.get(k) + 1);
                    else
                        occurrences.put(k, 1);
                }

                // find min and max occurrences
                for (Entry<String, Integer> entry : occurrences.entrySet()) {
                    if (min == null || min.getValue() > entry.getValue())
                        min = entry;
                    if (max == null || max.getValue() < entry.getValue())
                        max = entry;
                }

                // return min or max based on boolean parameter
                if (getTrendingUp)
                    test = max;
                else
                    test = min;
            }
            // perform lookup on test key and get row from table
            rs = JDBC.execQuery("select * from \"beverages\" where \"id\" = '" + test.getKey() + "'");
            while (rs.next()) {
                recs.add(new Beverage(
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
        return recs;
    }

    public ObservableList<Dessert> getRecDesserts(boolean getTrendingUp) {
        ObservableList<Dessert> recs = FXCollections.observableArrayList();
        try {
            LocalDate date_lower = beginDatePicker.getValue(); // get from gui
            LocalDate date_upper = endDatePicker.getValue(); // get from gui
            String sql = "select \"date\", \"order\" from order_data";
            ResultSet rs = JDBC.execQuery(sql);

            Map<String, Integer> occurrences = new HashMap<String, Integer>();
            Map<String, String[]> json;

            Entry<String, Integer> min = null;
            Entry<String, Integer> max = null;

            Gson gson = new Gson();
            Type entMapType = new TypeToken<Map<String, String[]>>() {}.getType();

            Entry<String, Integer> test = null;
            while (rs.next()) {
                // date object
                LocalDate date_db = rs.getDate("date").toLocalDate();

                // check if date in range
                if (date_db.compareTo(date_upper) > 0) // greater than upper bound, exit
                    break;
                else if (date_db.compareTo(date_lower) < 0) // less than lower bound, continue
                    continue;

                // convert json to Map<String, String[]>
                json = gson.fromJson(rs.getString("order"), entMapType);

                // count occurrences of item IDs discard customization suffix
                for (String k : json.keySet()) {
                    k = k.split("_")[0];
                    if (k.charAt(0) != 'D')
                        continue;
                    if (occurrences.containsKey(k))
                        occurrences.put(k, occurrences.get(k) + 1);
                    else
                        occurrences.put(k, 1);
                }

                // find min and max occurrences
                for (Entry<String, Integer> entry : occurrences.entrySet()) {
                    if (min == null || min.getValue() > entry.getValue())
                        min = entry;
                    if (max == null || max.getValue() < entry.getValue())
                        max = entry;
                }

                // return min or max based on boolean parameter
                if (getTrendingUp)
                    test = max;
                else
                    test = min;
            }
            // perform lookup on test key and get row from table
            rs = JDBC.execQuery("select * from \"desserts\" where \"id\" = '" + test.getKey() + "'");
            while (rs.next()) {
                recs.add(new Dessert(
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
        return recs;
    }

    @FXML
    public void actionShowBeverages(ActionEvent event) throws IOException {

        TableColumn<Beverage, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Beverage, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Beverage, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        quantityColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Beverage, Integer> t) -> {
                    Beverage selectedBeverage = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    selectedBeverage.setAvailableQuantity(Integer.parseInt(t.getNewValue().toString()));
                    JDBC.execUpdate("UPDATE beverages SET \"availableQuantity\" = " + selectedBeverage.getAvailableQuantity() +  " WHERE id = '" + selectedBeverage.getId() + "'");
                }
        );
        quantityColumn.setCellFactory(TextFieldTableCell.<Beverage, Integer>forTableColumn(new IntegerStringConverter()));

        TableColumn<Beverage, Double> costColumn = new TableColumn<>("Cost to make");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));

        TableColumn<Beverage, Double> priceColumn = new TableColumn<>("Sale price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        priceColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Beverage, Double> t) -> {
                    Beverage selectedBeverage = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    selectedBeverage.setSalePrice(Double.parseDouble(t.getNewValue().toString()));
                    JDBC.execUpdate("UPDATE beverages SET \"salePrice\" = " + selectedBeverage.getSalePrice() +  " WHERE id = '" + selectedBeverage.getId() + "'");
                }
        );
        priceColumn.setCellFactory(TextFieldTableCell.<Beverage, Double>forTableColumn(new DoubleStringConverter()));

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

        TableColumn<Dessert, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        TableColumn<Dessert, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Dessert, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        quantityColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Dessert, Integer> t) -> {
                    Dessert selectedDessert = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    selectedDessert.setAvailableQuantity(Integer.parseInt(t.getNewValue().toString()));
                    JDBC.execUpdate("UPDATE desserts SET \"availableQuantity\" = " + selectedDessert.getAvailableQuantity() +  " WHERE id = '" + selectedDessert.getId() + "'");
                }
        );
        quantityColumn.setCellFactory(TextFieldTableCell.<Dessert, Integer>forTableColumn(new IntegerStringConverter()));

        TableColumn<Dessert, Double> costColumn = new TableColumn<>("Cost to make");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));

        TableColumn<Dessert, Double> priceColumn = new TableColumn<>("Sale price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        priceColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Dessert, Double> t) -> {
                    Dessert selectedDessert = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    selectedDessert.setSalePrice(Double.parseDouble(t.getNewValue().toString()));
                    JDBC.execUpdate("UPDATE desserts SET \"salePrice\" = " + selectedDessert.getSalePrice() +  " WHERE id = '" + selectedDessert.getId() + "'");
                }
        );
        priceColumn.setCellFactory(TextFieldTableCell.<Dessert, Double>forTableColumn(new DoubleStringConverter()));

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

        TableColumn<Side, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        TableColumn<Side, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Side, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        quantityColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Side, Integer> t) -> {
                    Side selectedSide = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    selectedSide.setAvailableQuantity(Integer.parseInt(t.getNewValue().toString()));
                    JDBC.execUpdate("UPDATE sides SET \"availableQuantity\" = " + selectedSide.getAvailableQuantity() +  " WHERE id = '" + selectedSide.getId() + "'");
                }
        );
        quantityColumn.setCellFactory(TextFieldTableCell.<Side, Integer>forTableColumn(new IntegerStringConverter()));

        TableColumn<Side, Double> costColumn = new TableColumn<>("Cost to make");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));

        TableColumn<Side, Double> priceColumn = new TableColumn<>("Sale price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        priceColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Side, Double> t) -> {
                    Side selectedSide = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    selectedSide.setSalePrice(Double.parseDouble(t.getNewValue().toString()));
                    JDBC.execUpdate("UPDATE sides SET \"salePrice\" = " + selectedSide.getSalePrice() +  " WHERE id = '" + selectedSide.getId() + "'");
                }
        );
        priceColumn.setCellFactory(TextFieldTableCell.<Side, Double>forTableColumn(new DoubleStringConverter()));

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

        TableColumn<Entree, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Entree, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Entree, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Entree, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        quantityColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Entree, Integer> t) -> {
                    Entree selectedEntree = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    selectedEntree.setAvailableQuantity(Integer.parseInt(t.getNewValue().toString()));
                    JDBC.execUpdate("UPDATE entrees SET \"availableQuantity\" = " + selectedEntree.getAvailableQuantity() +  " WHERE id = '" + selectedEntree.getId() + "'");
                }
        );
        quantityColumn.setCellFactory(TextFieldTableCell.<Entree, Integer>forTableColumn(new IntegerStringConverter()));

        TableColumn<Entree, Double> costColumn = new TableColumn<>("Cost to make");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));

        TableColumn<Entree, Double> priceColumn = new TableColumn<>("Sale price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        priceColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Entree, Double> t) -> {
                    Entree selectedEntree = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    selectedEntree.setSalePrice(Double.parseDouble(t.getNewValue().toString()));
                    JDBC.execUpdate("UPDATE entrees SET \"salePrice\" = " + selectedEntree.getSalePrice() +  " WHERE id = '" + selectedEntree.getId() + "'");
                }
        );
        priceColumn.setCellFactory(TextFieldTableCell.<Entree, Double>forTableColumn(new DoubleStringConverter()));

        TableColumn<Entree, Array> toppingsColumn = new TableColumn<>("Toppings");
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

        TableColumn<Topping, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Topping, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Topping, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        quantityColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Topping, Integer> t) -> {
                    Topping selectedTopping = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    selectedTopping.setAvailableQuantity(Integer.parseInt(t.getNewValue().toString()));
                    JDBC.execUpdate("UPDATE toppings SET \"availableQuantity\" = " + selectedTopping.getAvailableQuantity() +  " WHERE id = '" + selectedTopping.getId() + "'");
                }
        );
        quantityColumn.setCellFactory(TextFieldTableCell.<Topping, Integer>forTableColumn(new IntegerStringConverter()));

        TableColumn<Topping, Double> costColumn = new TableColumn<>("Cost to make");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));

        TableColumn<Topping, Double> priceColumn = new TableColumn<>("Sale price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        priceColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Topping, Double> t) -> {
                    Topping selectedTopping = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    selectedTopping.setSalePrice(Double.parseDouble(t.getNewValue().toString()));
                    JDBC.execUpdate("UPDATE toppings SET \"salePrice\" = " + selectedTopping.getSalePrice() +  " WHERE id = '" + selectedTopping.getId() + "'");
                }
        );
        priceColumn.setCellFactory(TextFieldTableCell.<Topping, Double>forTableColumn(new DoubleStringConverter()));

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

    @FXML
    public void actionShowRecBeverages(ActionEvent event) throws IOException {

        TableColumn<Beverage, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Beverage, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Beverage, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));

        TableColumn<Beverage, Double> costColumn = new TableColumn<>("Cost to make");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));

        TableColumn<Beverage, Double> priceColumn = new TableColumn<>("salePrice");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        inventoryTableView.getColumns().clear();
        inventoryTableView.setItems(getRecBeverages(trendingUp));
        inventoryTableView.getColumns().addAll(
                idColumn,
                nameColumn,
                quantityColumn,
                costColumn,
                priceColumn);

    }

    @FXML
    public void actionShowRecDesserts(ActionEvent event) throws IOException {

        TableColumn<Dessert, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        TableColumn<Dessert, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Dessert, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));

        TableColumn<Dessert, Double> costColumn = new TableColumn<>("Cost to make");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));

        TableColumn<Dessert, Double> priceColumn = new TableColumn<>("Sale price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        inventoryTableView.getColumns().clear();
        inventoryTableView.setItems(getRecDesserts(trendingUp));
        inventoryTableView.getColumns().addAll(
                idColumn,
                nameColumn,
                quantityColumn,
                costColumn,
                priceColumn);

    }

    @FXML
    public void actionShowRecEntrees(ActionEvent event) throws IOException {

        TableColumn<Entree, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Entree, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Entree, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Entree, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));

        TableColumn<Entree, Double> costColumn = new TableColumn<>("Cost to make");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costToMake"));

        TableColumn<Entree, Double> priceColumn = new TableColumn<>("Sale price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        TableColumn<Entree, Array> toppingsColumn = new TableColumn<>("Toppings");
        toppingsColumn.setCellValueFactory(new PropertyValueFactory<>("toppings"));

        inventoryTableView.getColumns().clear();
        inventoryTableView.setItems(getRecEntrees(trendingUp));
        inventoryTableView.getColumns().addAll(
                idColumn,
                nameColumn,
                typeColumn,
                quantityColumn,
                costColumn,
                priceColumn,
                toppingsColumn);

    }

    public void actionTopChoices(ActionEvent event) {
        var object = inventoryTableView.getItems().get(1);
        try {
            switch (object.getClass().getName()) {
                case "WhataPOS.Entree":
                    getRecEntrees(trendingUp);
                    actionShowRecEntrees(event);
                    break;
                case "WhataPOS.Beverage":
                    getRecBeverages(trendingUp);
                    actionShowRecBeverages(event);
                    break;
                case "WhataPOS.Dessert":
                    getRecDesserts(trendingUp);
                    actionShowRecDesserts(event);
                    break;
            }
        } catch(IOException ex) {
            System.out.println(ex.toString());
        }
    }
}
