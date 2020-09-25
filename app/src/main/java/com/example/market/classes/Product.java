package com.example.market.classes;

import android.net.Uri;

public class Product {
    private String name;
    private float price;
    private String imageURL;

    public Product(String n,String p,String i){
        name=n;
        price=Float.parseFloat(p);
        imageURL=i;
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
        return imageURL;
    }

    public void setImage(String URL) {
        this.imageURL = URL;
    }

    public float getPrice() {
        return price;
    }
}
