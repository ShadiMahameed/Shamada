package com.example.market.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DriverTakenOrderAdapter extends RecyclerView.Adapter<DriverTakenOrderAdapter.TakenOrderViewHolder> {

    Context context;
    ArrayList<QuanProduct> products;

    public DriverTakenOrderAdapter(ArrayList<QuanProduct> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public DriverTakenOrderAdapter.TakenOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DriverTakenOrderAdapter.TakenOrderViewHolder(LayoutInflater.from(context).inflate(R.layout.card_view_taken_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DriverTakenOrderAdapter.TakenOrderViewHolder holder, int position) {
        holder.prod_name.setText(products.get(position).getName());
        holder.prod_quan.setText(products.get(position).getQuantity());
        Picasso.get().load(products.get(position).getImageURL()).into(holder.prod_image);


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class TakenOrderViewHolder extends RecyclerView.ViewHolder {
        TextView prod_name,prod_quan;
        ImageView prod_image;


        public TakenOrderViewHolder(@NonNull View itemView) {
            super(itemView);
             prod_name = (TextView) itemView.findViewById(R.id.prodName);
             prod_quan = (TextView) itemView.findViewById(R.id.quan);
             prod_image = (ImageView) itemView.findViewById(R.id.prodPic);
        }
    }
}
