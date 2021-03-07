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

    Connection conn = null;
    Statement stmt = null;

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
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e){
            // Handle errors for Class.forName
            e.printStackTrace();
        }
    }

    public ResultSet execQuery(String sql_query) {
        try {
            return this.stmt.executeQuery(sql_query);
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
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }
    }
}
