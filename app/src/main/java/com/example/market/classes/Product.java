package com.example.market.classes;

import android.net.Uri;

public class Product {
    private String name;
    private float price;
    private String image;

    public Product(String n,String p,String i){
        name=n;
        price=Float.parseFloat(p);
        image=i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }
}
