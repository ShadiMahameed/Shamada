package com.example.market.classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<QuanProduct> Products=new ArrayList<QuanProduct>();
    private String Location;
    private String Price;
    private LocalDateTime DateAndTime;
    private String CostumerName;
    private String Status;
//ready new
    public Order() {
    }

    public Order(ArrayList<QuanProduct> products, String location, LocalDateTime dateAndTime, String costumerName) {
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

    public List<QuanProduct> getProducts() {
        return Products;
    }

    public void setProducts(ArrayList<QuanProduct> products) {
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

    public void setProducts(List<QuanProduct> products) {
        Products = products;
    }

    public LocalDateTime getDateAndTime() {
        return DateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
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