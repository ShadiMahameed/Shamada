package com.example.market;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.market.classes.CostumerOrderAdapter;
import com.example.market.classes.Product;
import com.example.market.classes.QuanProduct;
import com.example.market.classes.adminRecyclerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CostumerOrder extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference  myRef;
    RecyclerView recyclerView;
    ArrayList<QuanProduct> products;
    CostumerOrderAdapter adapter;
    Product product;
    ImageButton btnClear,btnGotoCart;
    Button signout;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costumer_order);
        signout = findViewById(R.id.signout_costumr);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));

            }
        });

        recyclerView=(RecyclerView) findViewById(R.id.recyclerOrderCostumer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        products=new ArrayList<>();
        btnClear=(ImageButton) findViewById(R.id.btnclearCart);
        btnGotoCart=(ImageButton) findViewById(R.id.btngoCart);
        btnGotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CostumerOrder.this,MakeOrder.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList("products", (products));
                intent.putExtras(b);
                Toast.makeText(getApplicationContext(),"Moved To Payment",Toast.LENGTH_LONG).show();
                startActivity(intent);
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
                    products.add(new QuanProduct(product,"0"));
                }
                adapter = new CostumerOrderAdapter(CostumerOrder.this,products);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        findViewById(R.id.btnclearCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

//this is for commit

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