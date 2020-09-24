package com.example.market.classes;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.opengl.Matrix;

public class Product {
    private String name;
    private float price;
 //   private Drawable image;

    public Product(String n,String p){//, Drawable i){
        name=n;
        price=Float.parseFloat(p);
       // image=i;
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
      /*
    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
          */
    public float getPrice() {
        return price;
    }
}
