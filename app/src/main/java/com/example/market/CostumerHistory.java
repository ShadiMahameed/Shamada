package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.market.classes.CostumerOrderAdapter;
import com.example.market.classes.Order;
import com.example.market.classes.Product;
import com.example.market.classes.QuanProduct;
import com.example.market.classes.costumer_history_adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CostumerHistory extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference DeliveredOrderssDB;
    RecyclerView recyclerView;
    ArrayList<Order> orders = new ArrayList<>();
    costumer_history_adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costumer_history);

        recyclerView=(RecyclerView) findViewById(R.id.history_orders_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        DeliveredOrderssDB = database.getReference("DeliveredOrders");

        DeliveredOrderssDB.child(MainActivity.getUser().getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orders.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if (dataSnapshot.exists()) {
                        String s = dataSnapshot.getValue(String.class);
                        orders.add(new Gson().fromJson(s, Order.class));
                    }
                }
                adapter = new costumer_history_adapter(CostumerHistory.this,orders);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_history);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_history:
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), CostumerOrder.class));
                        overridePendingTransition(0,0);
                        break;

                }
                return false;
            }
        });
    }
}