package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.classes.DriverTakenOrderAdapter;
import com.example.market.classes.Order;
import com.example.market.classes.QuanProduct;
import com.example.market.classes.User;
import com.example.market.classes.driverOrdersAdapter;
import com.google.android.gms.ads.internal.gmsg.HttpClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.channels.AsynchronousChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class taken_order extends AppCompatActivity {

    User tempuser;
    Random random = new Random();
    FirebaseDatabase database;
    DatabaseReference TakenOrdersDB,deliveredOrdersDB,myRef;
    Button order_done;
    ImageButton goBack;
    TextView name,location,time,price,pay_method;
    Order order;
    RecyclerView recyclerView;
    ArrayList<QuanProduct> inventoryupdate,removeorder;
    Map<String, Object> invnew;
     DatabaseReference inventory,inventorydriver;
     User driver=MainActivity.getUser();
     long num;
     int count=0;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken_order);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");
        database = FirebaseDatabase.getInstance();
        TakenOrdersDB = database.getReference().child("TakenOrders");
        deliveredOrdersDB = database.getReference().child("DeliveredOrders");
        inventoryupdate=new ArrayList<QuanProduct>();
        removeorder=new ArrayList<QuanProduct>();
        invnew = new HashMap<>();

        inventory = database.getReference().child("Inventory").child(MainActivity.getUser().getName());
        inventorydriver = database.getReference().child("Inventory");

        recyclerView=(RecyclerView) findViewById(R.id.takenordersrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        goBack=(ImageButton) findViewById(R.id.go_back);
        order_done=(Button) findViewById(R.id.order_done);
        name = findViewById(R.id.costumer_name);
        location = findViewById(R.id.location);
        time = findViewById(R.id.date_time);
        price = findViewById(R.id.price);
        pay_method = findViewById(R.id.payingmeth);

        Query getOrder = TakenOrdersDB.child(MainActivity.getUser().getName());
        getOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String s = snapshot.getValue(String.class);
                    order = new Gson().fromJson(s, Order.class);
                    removeorder=(ArrayList<QuanProduct>) order.getProducts();
                }
                fillstuff();
                recyclerView.setAdapter(new DriverTakenOrderAdapter((ArrayList< QuanProduct>) order.getProducts(),getApplicationContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                startActivity(new Intent(getApplicationContext(),DriverNavigation.class));

            }
        });

        inventory.addListenerForSingleValueEvent(new ValueEventListener() {
            QuanProduct product2;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                num=snapshot.getChildrenCount();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    product2 = dataSnapshot.getValue(QuanProduct.class);
                    inventoryupdate.add(product2);
                    count++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DriverNavigation.class));
            }
        });


        order_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateInventory();
                int permissionCheck= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
                if(permissionCheck== PackageManager.PERMISSION_GRANTED)
                {
                    Mymsg();
                }
            }
        });
    }

    private void Mymsg() {
        String costumername=order.getCostumerName();
        Query checkuser = myRef.child(costumername);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    tempuser=snapshot.getValue(User.class);
                    phone=tempuser.getPhone();
                    String Msg="Your Delivery from Shamada is outside , please meet the driver";
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(phone,null,Msg,null,null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //String phoneNumber=MainActivity.getUser().getPhone();
    }

    private void UpdateInventory() {
        int index;
        for(int i=0;i<removeorder.size();i++)
        {
            index=removeorder.get(i).find(inventoryupdate);
            if(index!=-1)
            {
                inventoryupdate.get(index).setQuantity((Float.parseFloat(inventoryupdate.get(index).getQuantity())-Float.parseFloat(removeorder.get(i).getQuantity()))+"");
            }
        }
        for(int i=0;i<inventoryupdate.size();i++)
        {
            invnew.put(inventoryupdate.get(i).getName(),inventoryupdate.get(i) );
        }
        inventorydriver.child(driver.getName()).updateChildren(invnew);


        TakenOrdersDB.child(MainActivity.getUser().getName()).removeValue();
        String json = new Gson().toJson(order);
        deliveredOrdersDB.child(order.getCostumerName()).push().setValue(json);
        Toast.makeText(getApplicationContext(),"Order delivered",Toast.LENGTH_LONG).show();
        finishAffinity();
        startActivity(new Intent(getApplicationContext(),DriverOrders.class));


    }

    private void fillstuff() {
        name.setText(order.getCostumerName());
        location.setText(order.getLocation());
        price.setText(order.getPrice());
        pay_method.setText(order.getPaymentMethod());
    }








}