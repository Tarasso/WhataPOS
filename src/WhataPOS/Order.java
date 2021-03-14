package WhataPOS;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import WhataPOS.JDBC;

public class Order {
    private Integer id;
    private String date;
    private Vector<String> order;

    public static String fname = "";
    public static String lname = "";
    public static String customer_id = "";

    public Order(Integer id, String customer_id, String date, Vector<String> order) {
        this.id = id;
        Order.customer_id = customer_id;
        this.date = date;
        this.order = order;
    }

    public Order(String firstName, String lastName) throws SQLException {
        fname = firstName;
        lname = lastName;
        this.id = -1;
        Order.customer_id = "";
        this.date = "";
        this.order = null;

        final String CUSTOMER_CHECK = "SELECT \"id\" FROM customer_data WHERE \"firstName\" = ? AND \"lastName\" = ?";

        PreparedStatement preparedStatement = JDBC.conn.prepareStatement(CUSTOMER_CHECK);

        preparedStatement.setString(1, fname);
        preparedStatement.setString(2, lname);

        ResultSet result = preparedStatement.executeQuery();

        if(result.next())
            customer_id = result.getString(1);
        else
        {
            ResultSet countRS = JDBC.execQuery("SELECT count(*) from customer_data");
            countRS.next();
            customer_id = "U" + (countRS.getInt(1) + 1);

            final String SQL_INSERT = "INSERT INTO customer_data (\"id\", \"firstName\", \"lastName\") VALUES (?,?,?)";
            PreparedStatement insertCustomer = JDBC.conn.prepareStatement(SQL_INSERT);
            insertCustomer.setString(1, customer_id);
            insertCustomer.setString(2, fname);
            insertCustomer.setString(3, lname);
            insertCustomer.executeUpdate();
        }
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getCustomer_id() {return customer_id;}
    public void setCustomer_id(String customer_id) {Order.customer_id = customer_id;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public Vector<String> getOrder() {return order;}
    public void setOrder(Vector<String> order) {this.order = order;}
}
