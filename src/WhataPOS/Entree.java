package WhataPOS;

import java.util.Vector;

public class Entree {
    private String id;
    private String name;
    private String type;
    private Integer availableQuantity;
    private Double costToMake;
    private Double salePrice;
    private Vector<String> toppings;

    public Entree(String id, String name, String type, Integer availableQuantity, Double costToMake, Double salePrice, Vector<String> toppings) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.availableQuantity = availableQuantity;
        this.costToMake = costToMake;
        this.salePrice = salePrice;
        this.toppings = toppings;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public Integer getAvailableQuantity() {return availableQuantity;}
    public void setAvailableQuantity(Integer availableQuantity) {this.availableQuantity = availableQuantity;}

    public Double getCostToMake() {return costToMake;}
    public void setCostToMake(Double costToMake) {this.costToMake = costToMake;}

    public Double getSalePrice() {return salePrice;}
    public void setSalePrice(Double salePrice) {this.salePrice = salePrice;}

    public Vector<String> getToppings() {return toppings;}
    public void setToppings(Vector<String> toppings) {this.toppings = toppings;}

}
