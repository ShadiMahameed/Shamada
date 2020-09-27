package com.example.market;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.market.classes.Product;
import com.example.market.classes.adminRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CostumerOrder extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference  myRef;
    RecyclerView recyclerView;
    ArrayList<Product> products;
    adminRecyclerAdapter adapter;
    Product product;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costumer_order);

        recyclerView=(RecyclerView) findViewById(R.id.recyclerOrderCostumer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        products=new ArrayList<Product>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Product");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Object o = snapshot.getChildren();
                     System.out.println(o.toString());
                    //product=dataSnapshot.getValue(Product.class);
                    products.add(product);
                }
                adapter = new adminRecyclerAdapter(CostumerOrder.this,products);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_history:
                        startActivity(new Intent(getApplicationContext(),CostumerHistory.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_home:
                        break;
                }
                return false;
            }
        });
    }
}