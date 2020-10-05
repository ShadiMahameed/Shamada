package com.example.market.classes;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class QuanProduct extends Product implements Parcelable {

    private String quantity="0";


    public QuanProduct()
    {

    }
    public QuanProduct(QuanProduct qp)
    {
        super(qp.getName(), qp.getPrice(), qp.getImageURL());
        quantity=qp.getQuantity();
    }
    public QuanProduct(String n, String p, String i) {
        super(n, p, i);
    }

    public QuanProduct(Product p,String quan){
        super(p);
        quantity=quan;
    }
//this is for commit

    protected QuanProduct(Parcel in) {
        quantity = in.readString();
        name = in.readString();
        price = in.readString();
        imageURL = in.readString();
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public static final Creator<QuanProduct> CREATOR = new Creator<QuanProduct>() {
        @Override
        public QuanProduct createFromParcel(Parcel in) {
            return new QuanProduct(in);
        }

        @Override
        public QuanProduct[] newArray(int size) {
            return new QuanProduct[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quantity);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(imageURL);

    }
    public int find(ArrayList<QuanProduct> p)
    {
     for(int i =0 ;i<p.size();i++)
     {
         if(name.equals(p.get(i).getName()))
         {
             return i;
         }
     }
     return -1;
    }
}
