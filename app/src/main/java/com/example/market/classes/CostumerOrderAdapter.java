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

public class CostumerOrderAdapter extends RecyclerView.Adapter<CostumerOrderAdapter.MyViewHolder> {

    Context context;
    ArrayList<Product> products;

    public CostumerOrderAdapter(Context c , ArrayList<Product> p )
    {
        context=c;
        products=p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_view_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(products.get(position).getName());
        holder.price.setText(products.get(position).getPrice());
        Picasso.get().load(products.get(position).getImageURL()).into(holder.photo);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,price;
        ImageView photo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.proname);
            price=(TextView) itemView.findViewById(R.id.proprice);
            photo=(ImageView) itemView.findViewById(R.id.productPhoto);

        }
    }
}
