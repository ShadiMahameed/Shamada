package com.example.market.classes;

import java.util.List;

public class Inventory {
    private List<QuanProduct> products;
    private String DriverName;

    public Inventory(List<QuanProduct> products, String driverName) {
        this.products = products;
        DriverName = driverName;
    }

    public List<QuanProduct> getProducts() {
        return products;
    }

    public void setProducts(List<QuanProduct> products) {
        this.products = products;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }
}
