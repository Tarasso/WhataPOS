package WhataPOS;

import com.google.gson.Gson;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.*;

import java.net.URL;
import java.sql.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import WhataPOS.JDBC;
import javafx.stage.Window;

import javax.swing.plaf.basic.BasicDesktopIconUI;

public class OrderScreenController implements Initializable {

    // Element tag ids
    @FXML private TableView menuTableView;
    @FXML private TableView orderTableView;

    @FXML private TextArea orderTextArea;

    @FXML private Button topChoicesButton;
    @FXML private Button editToppingButton;
    @FXML private Button deleteButton;
    @FXML private Button payButton;

    private final double TAXPERCENT = 1.0825;

    // Standard alert box for different uses
    public static void showAlert(Alert.AlertType alertType, Window owner, String message, String title) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initOwner(owner);
        alert.show();
    }

    public int checkAvailableQuantity(String table, String id) throws SQLException {
        String SQL = "SELECT \"availableQuantity\" FROM " + table + " WHERE id = " + "'" + id + "'";
        ResultSet quantityRS = JDBC.execQuery(SQL);
        quantityRS.next();

        return quantityRS.getInt(1);
    }

    public void updateAvailableQuantity(String table, String id, int newQuantity){
        String SQL = "UPDATE " + table + " SET \"availableQuantity\" = newQuantity WHERE id = '" + id + "'";
        JDBC.execQuery(SQL);
    }

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

    // Gathers data for entrees for database
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


    // Get recommended entrees
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

    // Get recommended beverages
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

    // Get recommended desserts
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

    // Buttons
    @FXML
    public void actionWelcomeScreen(ActionEvent event) throws IOException {
        Parent welcomeScreenParent = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        Scene welcomeScreenScene = new Scene(welcomeScreenParent);

        // Reset customer
        Order.fname = "";
        Order.lname = "";
        Order.customer_id = "";

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(welcomeScreenScene);
        window.show();
    }

    @FXML
    public void actionShowEntrees(ActionEvent event) throws IOException {

        TableColumn<Entree, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Entree, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        menuTableView.getColumns().clear();
        menuTableView.setItems(getEntrees());
        menuTableView.getColumns().addAll(nameColumn, priceColumn);
        topChoicesButton.setVisible(true);

    }

    @FXML
    public void actionShowBeverages(ActionEvent event) throws IOException {
        TableColumn<Beverage, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Beverage, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        menuTableView.getColumns().clear();
        menuTableView.setItems(getBeverages());
        menuTableView.getColumns().addAll(nameColumn, priceColumn);
        topChoicesButton.setVisible(true);
    }

    @FXML
    public void actionShowDesserts(ActionEvent event) throws IOException {

        TableColumn<Dessert, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Dessert, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        menuTableView.getColumns().clear();
        menuTableView.setItems(getDesserts());
        menuTableView.getColumns().addAll(nameColumn, priceColumn);
        topChoicesButton.setVisible(true);
    }

    @FXML
    public void actionShowSides(ActionEvent event) throws IOException {

        TableColumn<Side, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Side, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        menuTableView.getColumns().clear();
        menuTableView.setItems(getSides());
        menuTableView.getColumns().addAll(nameColumn, priceColumn);
        topChoicesButton.setVisible(false);
    }

    @FXML
    public void actionShowRecBeverages(ActionEvent event) throws IOException {

        TableColumn<Beverage, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Beverage, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        menuTableView.getColumns().clear();
        menuTableView.setItems(getRecBeverages());
        menuTableView.getColumns().addAll(
                nameColumn,
                priceColumn);

    }

    @FXML
    public void actionShowRecDesserts(ActionEvent event) throws IOException {
        TableColumn<Dessert, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Dessert, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        menuTableView.getColumns().clear();
        menuTableView.setItems(getRecDesserts());
        menuTableView.getColumns().addAll(
                nameColumn,
                priceColumn);

    }

    @FXML
    public void actionShowRecEntrees(ActionEvent event) throws IOException {
        TableColumn<Entree, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Entree, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        menuTableView.getColumns().clear();
        menuTableView.setItems(getRecEntrees());
        menuTableView.getColumns().addAll(
                nameColumn,
                priceColumn);

    }

    @FXML
    public void actionTopChoices(ActionEvent event) {
        var object = menuTableView.getItems().get(1).getClass().getName();
        try {
            switch (object) {
                case "WhataPOS.Entree":
                    actionShowRecEntrees(event);
                    break;
                case "WhataPOS.Beverage":
                    actionShowRecBeverages(event);
                    break;
                case "WhataPOS.Dessert":
                    actionShowRecDesserts(event);
                    break;
            }
        } catch(IOException ex) {
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void actionSelectItem(ActionEvent event) throws IOException, SQLException {
        var object = menuTableView.getSelectionModel().getSelectedItem();


        Window menuTableOwner =  menuTableView.getScene().getWindow();
        Window orderTableOwner =  orderTableView.getScene().getWindow();

        if (Bindings.isEmpty(menuTableView.getSelectionModel().getSelectedItems()).get()) {
            showAlert(Alert.AlertType.ERROR, menuTableOwner, "No item is selected", "Error");
            return;
        }

        int quantity;

        double totalAfterTax = 0;

        switch (object.getClass().getName()) {
            case "WhataPOS.Beverage":
                Beverage selectedBeverage = (Beverage) object;

                quantity = checkAvailableQuantity("beverages", selectedBeverage.getId());

                if (quantity <= 0) {
                    showAlert(Alert.AlertType.ERROR, orderTableOwner, "Selected item is out of stock", "Error");
                    break;
                }
                else {
                    orderTableView.getItems().add(selectedBeverage);
                }

                break;

            case "WhataPOS.Entree":
                Entree selectedEntree = (Entree) object;
                quantity = checkAvailableQuantity("entrees", selectedEntree.getId());

                if (quantity <= 0) {
                    showAlert(Alert.AlertType.ERROR, orderTableOwner, "Selected item is out of stock", "Error");
                    break;
                }
                else {
                    // Switch to selecting toppings
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("SelectToppingScene.fxml")); // Loader targets the pop up window
                    Parent selectToppingParent = (Parent) loader.load();
                    Scene selectToppingScene = new Scene(selectToppingParent);
                    SelectToppingSceneController controller = loader.getController();

                    Stage window = new Stage(); // Set up the pop up screen
                    window.initModality(Modality.APPLICATION_MODAL);
                    window.setScene(selectToppingScene);

                    // Things to do before launch: set default toppings
                    String sql1 = "select toppings from entrees where id = \'" + selectedEntree.getId() + "\'";
                    ResultSet rs1 = JDBC.execQuery(sql1);
                    rs1.next();
                    var toppingsArray = rs1.getArray("toppings");
//                    var toppingsArray = selectedEntree.getToppings().getArray();
//                    String[] stringToppings = ((String[]) toppingsArray).clone();
                    String[] stringToppings = ((String[]) toppingsArray.getArray());
                    System.out.println("toppings (should be defaults):");
                    for(String s : stringToppings)
                        System.out.println(s);
                    // String[] stringToppings = (String[]) toppingsArray;
                    Vector<Topping> defaultToppings = new Vector<>();
                    for(String topping : stringToppings)
                    {
                        String sql = "select * from toppings where id = \'" + topping + "\'";
                        ResultSet rs = JDBC.execQuery(sql);
                        while (rs.next()) {
                            defaultToppings.add(new Topping(
                                    rs.getString("id"),
                                    rs.getString("name"),
                                    rs.getInt("availableQuantity"),
                                    rs.getDouble("costToMake"),
                                    rs.getDouble("salePrice")
                            ));
                        }
                    }
                    controller.setInitToppings(defaultToppings);

                    window.showAndWait();

                    // Things to do after launch: get new set of toppings
                    Vector<Topping> finalToppings = controller.getSelectedToppings();

                    Entree alteredEntree = new Entree(selectedEntree.getId(),
                                                      selectedEntree.getName(),
                                                      selectedEntree.getType(),
                                                      selectedEntree.getAvailableQuantity(),
                                                      selectedEntree.getCostToMake(),
                                                      selectedEntree.getSalePrice(),
                                                      selectedEntree.getToppings());

                    Array toppingsForDB = JDBC.conn.createArrayOf("text", finalToppings.toArray());
                    alteredEntree.setToppings(toppingsForDB);

                    double additionalCost = 0;
                    if (finalToppings.size() - defaultToppings.size() > 0) {
                        for (int i = defaultToppings.size(); i < finalToppings.size(); i++) {
                            additionalCost += finalToppings.get(i).getSalePrice() ;
                        }
                    }
                    alteredEntree.setSalePrice((int) ((alteredEntree.getSalePrice() + additionalCost) * 100) / 100.0);

                    // Put the new array of toppings here
//                    Array toppingsForDB = JDBC.conn.createArrayOf("text", stringToppings);
                    orderTableView.getItems().add(alteredEntree);

                }

                break;

            case "WhataPOS.Side":
                Side selectedSide = (Side) object;

                quantity = checkAvailableQuantity("sides", selectedSide.getId());

                if (quantity <= 0) {
                    showAlert(Alert.AlertType.ERROR, orderTableOwner, "Selected item is out of stock", "Error");
                    break;
                }

                orderTableView.getItems().add(selectedSide);
                break;

            case "WhataPOS.Dessert":
                Dessert selectedDessert = (Dessert) object;

                quantity = checkAvailableQuantity("desserts", selectedDessert.getId());

                if (quantity <= 0) {
                    showAlert(Alert.AlertType.ERROR, orderTableOwner, "Selected item is out of stock", "Error");
                    break;
                }

                orderTableView.getItems().add(selectedDessert);

                break;
        }

    }

    public void actionDeleteItem(ActionEvent event) throws IOException{

        Window orderTableOwner =  orderTableView.getScene().getWindow();

        if (Bindings.isEmpty(orderTableView.getItems()).get()) {
            showAlert(Alert.AlertType.ERROR, orderTableOwner, "Order is Empty", "Error");
            return;
        }

        var object = orderTableView.getSelectionModel().getSelectedItem();

        switch (object.getClass().getName()) {
            case "WhataPOS.Beverage":
                Beverage selectedBeverage = (Beverage) object;
                orderTableView.getItems().remove(selectedBeverage);
                break;

            case "WhataPOS.Entree":
                Entree selectedEntree = (Entree) object;
                orderTableView.getItems().remove(selectedEntree);
                break;

            case "WhataPOS.Side":
                Side selectedSide = (Side) object;
                orderTableView.getItems().remove(selectedSide);
                break;

            case "WhataPOS.Dessert":
                Dessert selectedDessert = (Dessert) object;
                orderTableView.getItems().remove(selectedDessert);
                break;
        }
    }


    public String convert(Vector items) throws SQLException {
        Gson gsonObj = new Gson();

        Map<String, Integer> id_occurences = new HashMap<>();
        Map<String, String[]> id_to_toppings = new HashMap<>();

        for(Object o : items)
        {
            if(o instanceof Entree)
            {
                Entree entree = (Entree) o;
                if(id_occurences.containsKey(entree.getId()))
                    id_occurences.put(entree.getId(), id_occurences.get(entree.getId()) + 1);
                else
                    id_occurences.put(entree.getId(), 0);

                String newId = entree.getId() + "_" + id_occurences.get(entree.getId());
                id_to_toppings.put(newId, (String[]) entree.getToppings().getArray());
            }
            else if(o instanceof Beverage)
            {
                Beverage beverage = (Beverage) o;
                if(id_occurences.containsKey(beverage.getId()))
                    id_occurences.put(beverage.getId(), id_occurences.get(beverage.getId()) + 1);
                else
                    id_occurences.put(beverage.getId(), 0);
            }
            else if(o instanceof Dessert)
            {
                Dessert dessert = (Dessert) o;
                if(id_occurences.containsKey(dessert.getId()))
                    id_occurences.put(dessert.getId(), id_occurences.get(dessert.getId()) + 1);
                else
                    id_occurences.put(dessert.getId(), 0);
            }
            else if(o instanceof Side)
            {
                Side side = (Side) o;
                if(id_occurences.containsKey(side.getId()))
                    id_occurences.put(side.getId(), id_occurences.get(side.getId()) + 1);
                else
                    id_occurences.put(side.getId(), 0);
            }
        }

        return gsonObj.toJson(id_to_toppings);
    }


    @FXML
    public void actionPayItem(ActionEvent event) throws IOException, SQLException {
        Window orderTableOwner = orderTableView.getScene().getWindow();

        if (Bindings.isEmpty(orderTableView.getItems()).get()) {
            showAlert(Alert.AlertType.ERROR, orderTableOwner, "Order is Empty", "Error");
            return;
        }

        // Finalize step
        if (payButton.getText().equals("Ready to Pay")) {
            deleteButton.setVisible(false);
            editToppingButton.setVisible(false);

            double orderTotal = 0;

            for (var item : orderTableView.getItems()) {
                switch (item.getClass().getName()) {
                    case "WhataPOS.Entree":
                        Entree entree = (Entree) item;
                        orderTotal += entree.getSalePrice();
                        System.out.println(orderTotal);
                        break;

                    case "WhataPOS.Beverage":
                        Beverage beverage = (Beverage) item;
                        orderTotal += beverage.getSalePrice();
                        break;

                    case "WhataPOS.Dessert":
                        Dessert dessert = (Dessert) item;
                        orderTotal += dessert.getSalePrice();
                        break;


                    case "WhataPOS.Side":
                        Side side = (Side) item;
                        orderTotal += side.getSalePrice();
                        break;
                }
            }

            orderTextArea.setText(
                    "Order Total: " + String.format("%.2f", orderTotal) + "\n"
                            + "Total After Tax: " + String.format("%.2f", orderTotal * TAXPERCENT)
            );

            payButton.setText("Pay");

        } else if (payButton.getText().equals("Pay")) {
            ObservableList totalOrder = orderTableView.getItems();


            Array orderIDsSQL;
            String[] orderIDsJAVA = new String[totalOrder.size()];
            Vector items = new Vector();

            for (int i = 0; i < totalOrder.size(); i++) {

                var orderElement = totalOrder.get(i);

                switch (totalOrder.get(i).getClass().getName()) {

                    case "WhataPOS.Beverage":
                        Beverage beverage = (Beverage) orderElement;
                        orderIDsJAVA[i] = beverage.getId();
                        items.add(beverage);
                        break;

                    case "WhataPOS.Entree":
                        Entree entree = (Entree) orderElement;
                        orderIDsJAVA[i] = entree.getId();
                        items.add(entree);
//                    System.out.println(entree.getName());
//                    var toppingsArray = entree.getToppings().getArray();
//                    String[] stringToppings = (String[]) toppingsArray;
//                    for(String s : stringToppings)
//                        System.out.println(s);
                        break;

                    case "WhataPOS.Side":
                        Side side = (Side) orderElement;
                        orderIDsJAVA[i] = side.getId();
                        items.add(side);
                        break;

                    case "WhataPOS.Dessert":
                        Dessert dessert = (Dessert) orderElement;
                        orderIDsJAVA[i] = dessert.getId();
                        items.add(dessert);
                        break;

                }

            }


//        System.out.println("max current id: " + maxid);
            ZoneId z = ZoneId.of("America/Chicago");
            LocalDate date = LocalDate.now(z);

            // TODO process items vector
            String jsonToInsert = convert(items);

            //String dummyJson = "{\"E5_0\": [\"T1\", \"T3\", \"T7\", \"T8\", \"T9\"], \"E1_0\": [\"T1\", \"T7\", \"T8\", \"T9\", \"T10\", \"T11\"], \"E7_0\": [\"T1\", \"T7\", \"T8\", \"T9\", \"T10\", \"T11\"], \"S4_0\": [], \"S1_0\": [], \"S3_0\": [], \"B4_0\": [], \"B5_0\": [], \"B2_0\": [], \"D1_0\": [], \"D1_1\": []}";
            //PGObject jsonObj = new PGObject();

            orderIDsSQL = JDBC.conn.createArrayOf("text", orderIDsJAVA);

            String[] temp = (String[]) orderIDsSQL.getArray();

//        final String SQL_INSERT = "INSERT INTO order_data (\"id\", \"customer_id\", \"date\", \"order\") VALUES (?,?,?,cast(? as json))";
            final String SQL_INSERT = "INSERT INTO order_data (\"customer_id\", \"date\", \"order\") VALUES (?,?,cast(? as json))";

            PreparedStatement preparedStatement = JDBC.conn.prepareStatement(SQL_INSERT);

            System.out.println(Order.fname + " " + Order.lname);

//        preparedStatement.setInt(1, maxid);
            preparedStatement.setString(1, Order.customer_id);
            preparedStatement.setObject(2, date);
            preparedStatement.setString(3, jsonToInsert);

            preparedStatement.executeUpdate();

            ResultSet maxidRS = JDBC.execQuery("SELECT MAX(\"id\") as maxid from order_data");
            maxidRS.next();
            int maxid = maxidRS.getInt("maxid");

            orderTextArea.setText(
                    "Order Placed! Your Order's ID is " + maxid + "."
            );
            orderTableView.getItems().clear();
        }
    }




    @Override
    public void initialize (URL url, ResourceBundle resourcebundle) {
        orderTextArea.setEditable(false);

        TableColumn<Side, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Side, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(120);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        orderTableView.getColumns().addAll(nameColumn, priceColumn);

        topChoicesButton.setVisible(false);
        editToppingButton.setVisible(false);
    }

}
