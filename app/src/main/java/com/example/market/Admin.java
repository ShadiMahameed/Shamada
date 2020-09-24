package com.example.market;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.market.classes.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Admin extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Product> products;

    private TextView showprice;
    private TextView showname;
    private ImageView prodimage;
    private Button nextprod;
    private EditText prodname;
    private EditText prodprice;
    private ImageView newimage;
    private ImageButton addimage;
    private Button addprod;

    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        showprice = findViewById(R.id.showPrice);
        showname = findViewById(R.id.showName);
        prodimage = findViewById(R.id.showImage);
        nextprod = findViewById(R.id.nextProduct);
        prodname = findViewById(R.id.ProductName);/////
        prodprice = findViewById(R.id.ProductPrice);////////////
        newimage = findViewById(R.id.newImage);
        addimage = findViewById(R.id.AddPic);

        findViewById(R.id.addProduct).setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Product");



    }
    @Override
    public void onClick(View v) {
        AddNewProduct();
    }

    private void AddNewProduct(){
        Product p =new Product(prodname.getText().toString().trim(),prodprice.getText().toString().trim());//, newimage.getDrawable());
        myRef.push().setValue(p);

    }



}