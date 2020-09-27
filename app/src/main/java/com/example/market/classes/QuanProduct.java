package com.example.market.classes;


public class QuanProduct extends Product {

    private int quantity=0;

    public QuanProduct(String n, String p, String i) {
        super(n, p, i);
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
