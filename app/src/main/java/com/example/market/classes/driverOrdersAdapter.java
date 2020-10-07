package com.example.market.classes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.IntentCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market.DriverNavigation;
import com.example.market.DriverOrders;
import com.example.market.MainActivity;
import com.example.market.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;
import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static com.google.android.gms.auth.api.signin.GoogleSignIn.requestPermissions;

public class driverOrdersAdapter extends RecyclerView.Adapter<driverOrdersAdapter.ViewHolder> {
//5osh b6eze
    ArrayList<Order> orders;
    ArrayList<String> nameInDB;
    Context context;

    public driverOrdersAdapter(ArrayList<Order> orders, ArrayList<String> nameInDB, Context context) {
        this.orders = orders;
        this.nameInDB = nameInDB;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.un_taken_order, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(orders.get(position).getCostumerName());
        holder.location.setText(orders.get(position).getLocation());
        holder.time.setText(orders.get(position).getDateAndTime());


        holder.takeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference UntakenOrders = database.getReference().child("UnTakenOrders");
                DatabaseReference TakenOrdersDB = database.getReference().child("TakenOrders");

                UntakenOrders.child(nameInDB.get(position)).removeValue();
                String json = new Gson().toJson(orders.get(position));
                TakenOrdersDB.child(MainActivity.getUser().getName()).setValue(json);
                Toast.makeText(context,"Order Taken",Toast.LENGTH_LONG).show();
                move(position);





            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,location,time;
        Button takeOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.costumerName);
            location = itemView.findViewById(R.id.Location);
            time = itemView.findViewById(R.id.Time);
            takeOrder =itemView.findViewById(R.id.Choose);
        }
    }



    void move(int position) {




        Intent i = new Intent(context, DriverNavigation.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }




}
