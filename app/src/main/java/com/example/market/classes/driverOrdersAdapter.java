package com.example.market.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class driverOrdersAdapter extends RecyclerView.Adapter<driverOrdersAdapter.ViewHolder> {

    ArrayList<Order> orders = new ArrayList<Order>();
    Context context;

    public driverOrdersAdapter(ArrayList<Order> orders, Context context) {
        this.orders = orders;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(orders.get(position).getCostumerName());
        holder.location.setText(orders.get(position).getLocation());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a z");
//
//        LocalDateTime f=orders.get(position).getDateAndTime();
//        String time="";
//        if(f != null)
//            time = formatter.format(f);
//        if(!time.isEmpty() && time!=null)
//            holder.time.setText(time);
        holder.takeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //todo ordeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeer


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

}
