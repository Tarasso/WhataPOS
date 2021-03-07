package WhataPOS;

public class Employee {
    private String id;
    private String name;
    private String username;
    private String password;
    private String homeAddress;
    private String phoneNumber;
    private String email;
    private String position;

    public Employee(String id, String name, String username, String password, String homeAddress, String phoneNumber, String email, String position) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.homeAddress = homeAddress;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.position = position;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getHomeAddress() {return homeAddress;}
    public void setHomeAddress(String homeAddress) {this.homeAddress = homeAddress;}

    public String getPhoneNumber() {return phoneNumber;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPosition() {return position;}
    public void setPosition(String position) {this.position = position;}
}
