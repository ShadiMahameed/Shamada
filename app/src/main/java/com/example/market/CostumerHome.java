package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CostumerHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costumer_profile);

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
                    case R.id.nav_settings:
                        startActivity(new Intent(getApplicationContext(),CostumerSettings.class));
                        overridePendingTransition(0,0);
                        break;

                }
                return false;
            }
        });
    }
}