package com.example.market.classes;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market.R;

import java.io.IOException;
import java.util.ArrayList;

public class adminRecyclerAdapter extends RecyclerView.Adapter<adminRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> products;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.productName);
            price=itemView.findViewById(R.id.productPrice);
            image=itemView.findViewById(R.id.productImage);
        }

    }

    public adminRecyclerAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public adminRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_product,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adminRecyclerAdapter.ViewHolder holder, int position) {

        holder.name.setText(products.get(position).getName());
        holder.price.setText(products.get(position).getPrice());
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(products.get(position).getImage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.image.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
