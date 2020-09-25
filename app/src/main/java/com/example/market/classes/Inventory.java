package com.example.market.classes;

public class Inventory {
    private Product Product;
    private String Driver;
    private String Quantity;

    public Inventory() {
    }

    public Inventory(com.example.market.classes.Product product, String driver, String quantity) {
        Product = product;
        Driver = driver;
        Quantity = quantity;
    }

    public com.example.market.classes.Product getProduct() {
        return Product;
    }

    public void setProduct(com.example.market.classes.Product product) {
        Product = product;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
