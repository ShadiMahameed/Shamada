package com.example.market.classes;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DriverInventoryAdapter extends RecyclerView.Adapter<DriverInventoryAdapter.InventoryViewHolder> {

    Context context;
    ArrayList<QuanProduct> quanProducts;

    public DriverInventoryAdapter(Context c , ArrayList<QuanProduct> p)
    {
        context=c;
        quanProducts=p;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InventoryViewHolder(LayoutInflater.from(context).inflate(R.layout.card_view_inventory,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        holder.pronamein.setText(quanProducts.get(position).getName());
        holder.propricein.setText(quanProducts.get(position).getQuantity()+"");
        Picasso.get().load(quanProducts.get(position).getImageURL()).into(holder.proimagein);
    }

    @Override
    public int getItemCount() {
        return quanProducts.size();
    }

    class InventoryViewHolder extends RecyclerView.ViewHolder
    {
        TextView propricein,pronamein;
        ImageView proimagein;
        EditText addquan;
        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            pronamein = (TextView) itemView.findViewById(R.id.pronameinv);
            propricein = (TextView) itemView.findViewById(R.id.proquaninv);
            proimagein = (ImageView) itemView.findViewById(R.id.productPhotoinv);
            addquan = (EditText) itemView.findViewById(R.id.txtQttyinv);
        }
    }

}