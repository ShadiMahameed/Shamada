package com.example.market.classes;

import android.net.Uri;

public class Product {
    private String name;
    private String price;
    private String imageURL;

//

    public Product(String n,String p,String i){
        name=n;
        price=(p);
        imageURL=i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return imageURL;
    }

    public void setImage(String URL) {
        this.imageURL = URL;
    }

    public String getPrice() {
        return price;
    }
}
