package WhataPOS;

import java.sql.*;

public class JDBC {
    /* Ideally:
     * use env variables to initialize connection vars
     * create hardened user for application on sql server
     */

    // JDBC driver name and database URL
    String DB_URL;

    // Database credentials
    String USER;
    String PASS;

    static Connection conn = null;
    static Statement stmt = null;

    public JDBC(
        String DB_URL,
        String USER,
        String PASS
    ) {
        this.DB_URL = DB_URL;
        this.USER = USER;
        this.PASS = PASS;
    }

    public void newConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            // Open connection
            this.conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // Return query creation object
            this.stmt = this.conn.createStatement();
            System.out.println("Connection established");
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e){
            // Handle errors for Class.forName
            e.printStackTrace();
        }
    }

    public static ResultSet execQuery(String sql_query) {
        try {
            return stmt.executeQuery(sql_query);
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }
        return null;
    }

    public void closeConnection() {
        try {
            this.stmt.close();
            this.conn.close();
            System.out.println("Connection closed");
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }
    }
}
