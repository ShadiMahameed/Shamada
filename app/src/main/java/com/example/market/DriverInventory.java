package com.example.market;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.market.classes.DriverInventoryAdapter;
import com.example.market.classes.Product;
import com.example.market.classes.QuanProduct;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DriverInventory extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    DriverInventoryAdapter driverInventoryAdapter;
    RecyclerView recyclerViewinv;
    ArrayList<QuanProduct> products;
    Product product;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_inventory);




        recyclerViewinv=(RecyclerView) findViewById(R.id.recyclerInventoryDriver);
        recyclerViewinv.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewinv.setHasFixedSize(true);
        products=new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Product");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    product=dataSnapshot.getValue(Product.class);
                    products.add(new QuanProduct(product,0));
                }
                driverInventoryAdapter = new DriverInventoryAdapter(DriverInventory.this,products);
                recyclerViewinv.setAdapter(driverInventoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        BottomNavigationView bottomNavigationView=findViewById(R.id.driver_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_inventory);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_inventory:
                        break;
                    case R.id.nav_home_driver:
                        startActivity(new Intent(getApplicationContext(),DriverOrders.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_map:
                        startActivity(new Intent(getApplicationContext(),DriverNavigation.class));
                        overridePendingTransition(0,0);
                        break;
                }

                return false;
            }
        });
    }
}