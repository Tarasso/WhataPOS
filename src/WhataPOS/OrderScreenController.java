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
import javafx.stage.Stage;
import java.time.*;

import javax.swing.*;
import java.net.URL;
import java.sql.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Date;

import WhataPOS.JDBC;

public class OrderScreenController implements Initializable {

    // Element tag ids
    @FXML private TableView menuTableView;
    @FXML private TextArea orderTextArea;
    @FXML private TableView orderTableView;
    private float orderTotal = 0;
    private final double TAXPERCENT = 1.0825;




    // Gathers data for beverages from database
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

    // Gathers data for Entrees for database
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

    // Gathers data for desserts from database
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

    // Gathers data for sides from database
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

    // Buttons
    @FXML
    public void actionWelcomeScreen(ActionEvent event) throws IOException {
        Parent welcomeScreenParent = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        Scene welcomeScreenScene = new Scene(welcomeScreenParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(welcomeScreenScene);
        window.show();
    }

    @FXML
    public void actionShowEntrees(ActionEvent event) throws IOException {

        TableColumn<Entree, String> nameColumn = new TableColumn<>("name");
        nameColumn.setMinWidth(20);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Entree, String> priceColumn = new TableColumn<>("salePrice");
        priceColumn.setMinWidth(20);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        menuTableView.getColumns().clear();
        menuTableView.setItems(getEntrees());
        menuTableView.getColumns().addAll(nameColumn, priceColumn);

    }

    @FXML
    public void actionShowBeverages(ActionEvent event) throws IOException {

        TableColumn<Beverage, String> nameColumn = new TableColumn<>("name");
        nameColumn.setMinWidth(20);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Beverage, String> priceColumn = new TableColumn<>("salePrice");
        priceColumn.setMinWidth(20);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        menuTableView.getColumns().clear();
        menuTableView.setItems(getBeverages());
        menuTableView.getColumns().addAll(nameColumn, priceColumn);
    }

    @FXML
    public void actionShowDesserts(ActionEvent event) throws IOException {

        TableColumn<Dessert, String> nameColumn = new TableColumn<>("name");
        nameColumn.setMinWidth(20);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Dessert, String> priceColumn = new TableColumn<>("salePrice");
        priceColumn.setMinWidth(20);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        menuTableView.getColumns().clear();
        menuTableView.setItems(getDesserts());
        menuTableView.getColumns().addAll(nameColumn, priceColumn);
    }

    @FXML
    public void actionShowSides(ActionEvent event) throws IOException {

        TableColumn<Side, String> nameColumn = new TableColumn<>("name");
        nameColumn.setMinWidth(20);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Side, String> priceColumn = new TableColumn<>("salePrice");
        priceColumn.setMinWidth(20);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        menuTableView.getColumns().clear();
        menuTableView.setItems(getSides());
        menuTableView.getColumns().addAll(nameColumn, priceColumn);
    }

    @FXML
    public void actionShowToppings(ActionEvent event) throws IOException {

    }

    @FXML
    public void actionSelectItem(ActionEvent event) throws IOException {
        var object = menuTableView.getSelectionModel().getSelectedItem();

        double totalAfterTax = 0;

        switch (object.getClass().getName()) {
            case "WhataPOS.Beverage":
                Beverage selectedBeverage = (Beverage) object;
                orderTableView.getItems().add(selectedBeverage);

                orderTotal += selectedBeverage.getSalePrice();
                totalAfterTax = orderTotal * TAXPERCENT;

                orderTextArea.setText(
                        "Order Total: " + String.format("%.2f", orderTotal) + "\n"
                                + "Total After Tax: " + String.format("%.2f", totalAfterTax)
                );

                break;

            case "WhataPOS.Entree":
                Entree selectedEntree = (Entree) object;

                orderTableView.getItems().add(selectedEntree);

                orderTotal += selectedEntree.getSalePrice();
                totalAfterTax = orderTotal * TAXPERCENT;

                orderTextArea.setText(
                        "Order Total: " + String.format("%.2f", orderTotal) + "\n"
                                + "Total After Tax: " + String.format("%.2f", totalAfterTax)
                );

                break;

            case "WhataPOS.Side":
                Side selectedSide = (Side) object;
                orderTableView.getItems().add(selectedSide);

                orderTotal += selectedSide.getSalePrice();
                totalAfterTax = orderTotal * TAXPERCENT;

                orderTextArea.setText(
                        "Order Total: " + String.format("%.2f", orderTotal) + "\n"
                                + "Total After Tax: " + String.format("%.2f", totalAfterTax)
                );

                break;

            case "WhataPOS.Dessert":
                Dessert selectedDessert = (Dessert) object;
                orderTableView.getItems().add(selectedDessert);

                orderTotal += selectedDessert.getSalePrice();
                totalAfterTax = orderTotal * TAXPERCENT;

                orderTextArea.setText(
                        "Order Total: " + String.format("%.2f", orderTotal) + "\n"
                                + "Total After Tax: " + String.format("%.2f", totalAfterTax)
                );

                break;
        }

    }

    public void actionDeleteItem(ActionEvent event) throws IOException{
        var object = orderTableView.getSelectionModel().getSelectedItem();


        double totalAfterTax = 0;

        switch (object.getClass().getName()) {
            case "WhataPOS.Beverage":
                Beverage selectedBeverage = (Beverage) object;

                orderTableView.getItems().remove(selectedBeverage);
                orderTotal -= selectedBeverage.getSalePrice();
                totalAfterTax = orderTotal * TAXPERCENT;

                orderTextArea.setText(
                        "Order Total: " + String.format("%.2f", orderTotal) + "\n"
                                + "Total After Tax: " + String.format("%.2f", totalAfterTax)
                );
                break;

            case "WhataPOS.Entree":
                Entree selectedEntree = (Entree) object;

                orderTableView.getItems().remove(selectedEntree);
                orderTotal -= selectedEntree.getSalePrice();
                totalAfterTax = orderTotal * TAXPERCENT;

                orderTextArea.setText(
                        "Order Total: " + String.format("%.2f", orderTotal) + "\n"
                                + "Total After Tax: " + String.format("%.2f", totalAfterTax)
                );


                break;

            case "WhataPOS.Side":
                Side selectedSide = (Side) object;

                orderTableView.getItems().remove(selectedSide);
                orderTotal -= selectedSide.getSalePrice();
                totalAfterTax = orderTotal * TAXPERCENT;

                orderTextArea.setText(
                        "Order Total: " + String.format("%.2f", orderTotal) + "\n"
                                + "Total After Tax: " + String.format("%.2f", totalAfterTax)
                );


                break;

            case "WhataPOS.Dessert":
                Dessert selectedDessert = (Dessert) object;

                orderTableView.getItems().remove(selectedDessert);
                orderTotal -= selectedDessert.getSalePrice();
                totalAfterTax = orderTotal * TAXPERCENT;

                orderTextArea.setText(
                        "Order Total: " + String.format("%.2f", orderTotal) + "\n"
                                + "Total After Tax: " + String.format("%.2f", totalAfterTax)
                );


                break;
        }

    }


    public void actionPayItem(ActionEvent event) throws IOException, SQLException {
        ObservableList totalOrder = orderTableView.getItems();

        Array orderIDsSQL;
        String[] orderIDsJAVA = new String[totalOrder.size()];


        List<String> orderIDsList = new ArrayList<String>();

        for (int i = 0; i < totalOrder.size(); i++){

            var orderElement = totalOrder.get(i);

            switch (totalOrder.get(i).getClass().getName()) {

                case "WhataPOS.Beverage":
                    Beverage beverage = (Beverage) orderElement;
                    //orderIDsJAVA[i] = beverage.getId();
                    orderIDsList.add(beverage.getId());
                    break;

                case "WhataPOS.Entree":
                    Entree entree = (Entree) orderElement;
                   // orderIDsJAVA[i] = entree.getId();
                    orderIDsList.add(entree.getId());
                    break;

                case "WhataPOS.Side":
                    Side side = (Side) orderElement;
                    //orderIDsJAVA[i] = side.getId();
                    orderIDsList.add(side.getId());
                    break;

                case "WhataPOS.Dessert":
                    Dessert dessert = (Dessert) orderElement;
                    //orderIDsJAVA[i] = dessert.getId();
                    orderIDsList.add(dessert.getId());
                    break;


            }

        }

        ResultSet maxidRS = JDBC.execQuery("SELECT MAX(\"id\") as maxid from order_data");
        maxidRS.next();
        int maxid = maxidRS.getInt("maxid") + 1;
        String date = LocalDate.now().toString();

        orderIDsSQL = JDBC.conn.createArrayOf("text", orderIDsJAVA);

        ResultSet newOrder = JDBC.execQuery("INSERT INTO order_data (\"id\", \"customer_id\", \"date\", \"order\")\n" +
                "VALUES (maxid, 'U-1', date, '{\"E4\",\"S2\",\"B2\"}');");

        System.out.println(date);
        System.out.println(maxid);


    }



    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        orderTextArea.setEditable(false);

        TableColumn<Side, String> nameColumn = new TableColumn<>("name");
        nameColumn.setMinWidth(20);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Side, String> priceColumn = new TableColumn<>("salePrice");
        priceColumn.setMinWidth(20);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        orderTableView.getColumns().addAll(nameColumn, priceColumn);
    }

}
