package com.example.market;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;

import com.example.market.classes.Product;
import com.example.market.classes.QuanProduct;

import java.util.ArrayList;

public class MakeOrder extends AppCompatActivity {

    ArrayList<QuanProduct> products;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);

        Bundle b = new Bundle( this.getIntent().getExtras());
        products = b.getParcelableArrayList("products");

        for(int i=0;i<products.size();i++){
            System.out.println(products.get(i).getQuantity() +" "+ products.get(i).getName());
        }

    }
}