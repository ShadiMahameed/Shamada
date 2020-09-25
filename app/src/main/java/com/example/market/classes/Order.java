package com.example.market.classes;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<Product> Products=new ArrayList<Product>();
    private String Location;
    private String Price;
    private DateTimeFormatter DateAndTime;
    private String CostumerName;
    private String Status;
//ready new
    public Order() {
    }

    public Order(ArrayList<Product> products, String location, DateTimeFormatter dateAndTime, String costumerName) {
        this.Products = products;
        Location = location;
        DateAndTime = dateAndTime;
        CostumerName = costumerName;
        Status = "Pending";
        Price = gettotalprice();
    }

    private String gettotalprice() {
        float totalprice = 0;
        String empty="";
        for (int i = 0; i < Products.size(); i++) {
            totalprice += Float.parseFloat(Products.get(i).getPrice());
        }
        return totalprice+empty;
    }

    public List<Product> getProducts() {
        return Products;
    }

    public void setProducts(ArrayList<Product> products) {
        Products = products;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public DateTimeFormatter getDateAndTime() {
        return DateAndTime;
    }

    public void setDateAndTime(DateTimeFormatter dateAndTime) {
        DateAndTime = dateAndTime;
    }

    public String getCostumerName() {
        return CostumerName;
    }

    public void setCostumerName(String costumerName) {
        CostumerName = costumerName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}