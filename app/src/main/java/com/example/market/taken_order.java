package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.classes.Order;
import com.example.market.classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Random;

public class taken_order extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference TakenOrdersDB,deliveredOrdersDB;
    Button goBack,order_done;
    TextView name,location,time,price,pay_method;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken_order);

        database = FirebaseDatabase.getInstance();
        TakenOrdersDB = database.getReference().child("takenOrders");
        TakenOrdersDB = database.getReference().child("deliveredOrders");


        name = findViewById(R.id.costumer_name);
        location = findViewById(R.id.location);
        time = findViewById(R.id.date_time);
        price = findViewById(R.id.price);
        pay_method = findViewById(R.id.payingmeth);

        Query getOrder = TakenOrdersDB.child(MainActivity.getUser().getName());
        getOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s = snapshot.getValue(String.class);
                order = new Gson().fromJson(s,Order.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                startActivity(new Intent(getApplicationContext(),DriverNavigation.class));

            }
        });

        name.setText(order.getCostumerName());
        location.setText(order.getLocation());
        //time.setText(order.getDateAndTime());
        price.setText(order.getPrice());
        pay_method.setText(order.getPaymentMethod());

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DriverNavigation.class));
            }
        });


        final Random R = new Random();
        order_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakenOrdersDB.child(MainActivity.getUser().getName()).removeValue();
                String json = new Gson().toJson(order);
                deliveredOrdersDB.child(order.getCostumerName()).child(R.nextInt()+"").setValue(json);
                Toast.makeText(getApplicationContext(),"Order delivered",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),DriverNavigation.class));
            }
        });




    }
}