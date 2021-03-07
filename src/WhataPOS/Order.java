package WhataPOS;

import java.util.Vector;

public class Order {
    private Integer id;
    private String customer_id;
    private String date;
    private Vector<String> order;

    public Order(Integer id, String customer_id, String date, Vector<String> order) {
        this.id = id;
        this.customer_id = customer_id;
        this.date = date;
        this.order = order;
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getCustomer_id() {return customer_id;}
    public void setCustomer_id(String customer_id) {this.customer_id = customer_id;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public Vector<String> getOrder() {return order;}
    public void setOrder(Vector<String> order) {this.order = order;}
}
