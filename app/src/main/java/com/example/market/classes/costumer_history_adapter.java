package com.example.market.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class costumer_history_adapter extends RecyclerView.Adapter<costumer_history_adapter.ViewHolder> {

    Context context;
    ArrayList<Order> orders;

    public costumer_history_adapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new costumer_history_adapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_view_history_order_costumer,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.date_time.setText(orders.get(position).getDateAndTime()); // todo
        holder.method.setText(orders.get(position).getPaymentMethod());
        holder.location.setText(orders.get(position).getLocation());
        holder.price.setText(orders.get(position).getPrice());
        holder.id.setText("Order ID: "+position);

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView price,location,method,id,date_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            price = (TextView) itemView.findViewById(R.id.price_);
            location = (TextView) itemView.findViewById(R.id.location_);
            method = (TextView) itemView.findViewById(R.id.payment_method_);
            id = (TextView) itemView.findViewById(R.id.order_id);
            date_time = (TextView) itemView.findViewById(R.id.date_time);
        }
    }
}
