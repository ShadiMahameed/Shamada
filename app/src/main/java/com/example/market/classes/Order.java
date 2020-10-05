package com.example.market.classes;

import android.content.pm.PackageManager;

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

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    private String PaymentMethod;
//ready new
    public Order() {
    }

    public Order(List<QuanProduct> products, String location, String price, LocalDateTime dateAndTime, String costumerName,String paymentMethod) {
        Products = products;
        Location = location;
        Price = price;
        DateAndTime = dateAndTime;
        CostumerName = costumerName;
        Status ="Pending";
        PaymentMethod=paymentMethod;
    }

   /* public Order(ArrayList<QuanProduct> products, String location, LocalDateTime dateAndTime, String costumerName) {
        this.Products = products;
        Location = location;
        DateAndTime = dateAndTime;
        CostumerName = costumerName;
        Status = "Pending";
        Price = gettotalprice();
    }*/


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