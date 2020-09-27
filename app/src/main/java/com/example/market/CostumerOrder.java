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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.market.classes.CostumerOrderAdapter;
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
    CostumerOrderAdapter adapter;
    Product product;
    ImageButton btnClear,btnGotoCart;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costumer_order);

        recyclerView=(RecyclerView) findViewById(R.id.recyclerOrderCostumer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        products=new ArrayList<>();
        btnClear=(ImageButton) findViewById(R.id.btnclearCart);
        btnGotoCart=(ImageButton) findViewById(R.id.btngoCart);
        btnGotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Move To Payment",Toast.LENGTH_LONG).show();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Cart is Empty",Toast.LENGTH_LONG).show();
            }
        });



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Product");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    product=dataSnapshot.getValue(Product.class);
                    products.add(product);
                }
                adapter = new CostumerOrderAdapter(CostumerOrder.this,products);
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