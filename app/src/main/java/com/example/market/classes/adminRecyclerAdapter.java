package com.example.market.classes;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class adminRecyclerAdapter extends RecyclerView.Adapter<adminRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> products;

    FirebaseDatabase database;
    DatabaseReference myRef;



    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        ImageView image;
        ImageButton delButton;
        int pos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.productName);
            price=itemView.findViewById(R.id.productPrice);
            image=itemView.findViewById(R.id.productImage);
            delButton=itemView.findViewById(R.id.deleteButton);
        }

    }

    public adminRecyclerAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
        this.database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Product");
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
    public void onBindViewHolder(@NonNull final adminRecyclerAdapter.ViewHolder holder, int position) {

        holder.name.setText(products.get(position).getName());
        holder.price.setText(products.get(position).getPrice());
        Picasso.get().load(products.get(position).getImageURL()).into(holder.image);
        holder.pos=position;
        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(products.get(holder.pos).getName()).removeValue();

            }
        });




    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
