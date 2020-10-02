package com.example.market.classes;

import android.net.Uri;

public class Product {
    protected String name;
    protected String price;
    protected String imageURL;


    public Product(String n,String p,String i){
        name=n;
        price=(p);
        imageURL=i;
    }
    public Product(Product p)
    {
        name=p.getName();
        price=p.getPrice();
        imageURL=p.getImageURL();
    }

    public Product() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

