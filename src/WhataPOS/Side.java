package WhataPOS;

public class Side {
    private String id;
    private String name;
    private String availableQuantity;
    private Double costToMake;
    private Double salePrice;

    public Side(String id, String name, String availableQuantity, Double costToMake, Double salePrice) {
        this.id = id;
        this.name = name;
        this.availableQuantity = availableQuantity;
        this.costToMake = costToMake;
        this.salePrice = salePrice;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getAvailableQuantity() {return availableQuantity;}
    public void setAvailableQuantity(String availableQuantity) {this.availableQuantity = availableQuantity;}

    public Double getCostToMake() {return costToMake;}
    public void setCostToMake(Double costToMake) {this.costToMake = costToMake;}

    public Double getSalePrice() {return salePrice;}
    public void setSalePrice(Double salePrice) {this.salePrice = salePrice;}
}
