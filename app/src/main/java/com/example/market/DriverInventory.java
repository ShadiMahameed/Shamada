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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.market.classes.DriverInventoryAdapter;
import com.example.market.classes.Product;
import com.example.market.classes.QuanProduct;
import com.example.market.classes.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverInventory extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef,myRefinv,myRefinvd;
    DriverInventoryAdapter driverInventoryAdapter;
    RecyclerView recyclerViewinv;
    ArrayList<QuanProduct> products=new ArrayList<QuanProduct>();
    Product product;
    QuanProduct product2;
    Button updateinv;
    Map<String, Object> invnew;
    User driver;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_inventory);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Product");
        myRefinv=database.getReference().child("Inventory");
        recyclerViewinv=(RecyclerView) findViewById(R.id.recyclerInventoryDriver);
        recyclerViewinv.setLayoutManager(new LinearLayoutManager(this));
        driver=MainActivity.getUser();
        invnew = new HashMap<>();
        myRefinvd=myRefinv.child(driver.getName());

        updateinv=(Button) findViewById(R.id.btnUpdateInv);
        updateinv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<products.size();i++)
                {
                    invnew.put(products.get(i).getName(),products.get(i) );
                }
                myRefinv.child(driver.getName()).updateChildren(invnew);
                Toast.makeText(getApplicationContext(),"Inventory has been updated",Toast.LENGTH_SHORT).show();
                //Intent intent=getIntent();
                finish();
                startActivity(getIntent());
            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    product=dataSnapshot.getValue(Product.class);
                    products.add(new QuanProduct(product,"0"));

                }
                getDriverinv();
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

    private void getDriverinv() {
        myRefinvd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0,index;
                QuanProduct temp;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    product2 = dataSnapshot.getValue(QuanProduct.class);
                    if (product2.find(products)!=-1) {
                        index=product2.find(products);
                        products.get(index).setQuantity(product2.getQuantity());
                    }

                }
                driverInventoryAdapter = new DriverInventoryAdapter(DriverInventory.this,(ArrayList<QuanProduct>) products);
                recyclerViewinv.setAdapter(driverInventoryAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}