package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.market.classes.CostumerOrderAdapter;
import com.example.market.classes.Driver;
import com.example.market.classes.DriverInventoryAdapter;
import com.example.market.classes.Inventory;
import com.example.market.classes.Order;
import com.example.market.classes.Product;
import com.example.market.classes.QuanProduct;
import com.example.market.classes.User;
import com.example.market.classes.driverOrdersAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DriverOrders extends AppCompatActivity {

    ArrayList<Order> orders = new ArrayList();
    ArrayList<String> nameInDB = new ArrayList<>();
    List<QuanProduct> quantproducts=new ArrayList<QuanProduct>();
    FirebaseDatabase database;
    DatabaseReference inventory,UntakenOrders,TakenOrders;
    User driver;
    ArrayList<QuanProduct> products = new ArrayList<QuanProduct>();
    Inventory inv;
    RecyclerView recyclerView;
    int index=0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_orders);



        recyclerView=(RecyclerView) findViewById(R.id.recyclerorderdriver);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        driver=MainActivity.getUser();
        database = FirebaseDatabase.getInstance();
        TakenOrders = database.getReference().child("TakenOrders");
        UntakenOrders=database.getReference().child("UnTakenOrders");

        inventory = database.getReference().child("Inventory").child(MainActivity.getUser().getName());

        Query getOrder = TakenOrders.child(MainActivity.getUser().getName());
        getOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Toast.makeText(getApplicationContext(),"Deliver order first",Toast.LENGTH_LONG).show();
                }
                else
                {

                    inventory.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            products.clear();
                            QuanProduct p;
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                p = dataSnapshot.getValue(QuanProduct.class);
                                products.add(p);
                            }
                            inv = new Inventory(products,driver.getName());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    UntakenOrders.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Order o = new Gson().fromJson(dataSnapshot.getValue(String.class),Order.class);
                                quantproducts=o.getProducts();
                                boolean flag=true;
                                for(int i=0;i<quantproducts.size();i++)
                                {
                                    if(inv.getProducts().contains(quantproducts.get(i)))
                                        index=inv.getProducts().indexOf(quantproducts.get(i));
                                    if(Integer.parseInt(quantproducts.get(i).getQuantity())>Integer.parseInt(inv.getProducts().get(index).getQuantity()))
                                    {
                                        flag=false;
                                    }
                                }
                                if(flag==true)
                                {
                                    orders.add(o);
                                    nameInDB.add(dataSnapshot.getKey());
                                }
                            }

                            recyclerView.setAdapter(new driverOrdersAdapter(orders,nameInDB,getApplicationContext()));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.driver_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home_driver);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_inventory:
                        startActivity(new Intent(getApplicationContext(),DriverInventory.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_home_driver:
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