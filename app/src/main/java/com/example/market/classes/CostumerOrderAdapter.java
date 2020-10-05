package com.example.market.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.market.CostumerOrder;
import com.example.market.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CostumerOrderAdapter extends RecyclerView.Adapter<CostumerOrderAdapter.MyViewHolder> {

    Context context;
    ArrayList<QuanProduct> products;

    public CostumerOrderAdapter(Context c , ArrayList<QuanProduct> p )
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.name.setText(products.get(position).getName());
        holder.price.setText(products.get(position).getPrice());
        holder.quantity.setHint(products.get(position).getQuantity());
        Picasso.get().load(products.get(position).getImageURL()).into(holder.photo);

        holder.changeQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((Pattern.compile("[0-9]*+").matcher(holder.quantity.getText().toString().trim()).matches())) {
                    products.get(position).setQuantity(holder.quantity.getText().toString().trim());
                    Toast.makeText(v.getContext(), "Quantity changed in the chart", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(v.getContext(), "Quantity Should be Integer", Toast.LENGTH_LONG).show();

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,price;
        EditText quantity;
        ImageView photo;
        ImageButton changeQuan;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            quantity=(EditText) itemView.findViewById(R.id.txtQtty);
            name=(TextView) itemView.findViewById(R.id.proname);
            price=(TextView) itemView.findViewById(R.id.proprice);
            photo=(ImageView) itemView.findViewById(R.id.productPhoto);
            changeQuan=(ImageButton) itemView.findViewById(R.id.imageButton);

        }
    }
}
