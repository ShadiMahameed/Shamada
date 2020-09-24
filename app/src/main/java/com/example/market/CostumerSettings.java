package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CostumerSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costumer_settings);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_settings);
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
                        startActivity(new Intent(getApplicationContext(),CostumerHome.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_settings:
                        break;

                }
                return false;
            }
        });
    }
}