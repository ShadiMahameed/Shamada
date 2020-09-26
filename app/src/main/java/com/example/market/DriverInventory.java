package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DriverInventory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_inventory);

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
                        startActivity(new Intent(getApplicationContext(),DriverInventory.class));
                        overridePendingTransition(0,0);
                        break;
                }

                return false;
            }
        });
    }
}