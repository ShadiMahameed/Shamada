package com.example.market.classes;

import java.util.ArrayList;

public class Order {
    private ArrayList<Product> products;
    private float price;






    private void calculatePrice(){
        price=20;
        for(int i=0;i<products.size();i++) {
            price+=products.get(i).getPrice();
        }
    }


}
