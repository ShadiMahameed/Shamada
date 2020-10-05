package com.example.market;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.classes.Order;
import com.example.market.classes.QuanProduct;
import com.example.market.classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

public class MakeOrder extends AppCompatActivity {

    ArrayList<QuanProduct> products;

    float f;
    String txtid , txtccnum , txtmonth , txtyear , txtcvv,payementmethod;
    EditText vid,vccnum,vmonth,vyear,vcvv;

    TextView userN, FinalPrice;
    EditText location, phone;
    Button payCash, payCC, returnbtn;
    Dialog ccdialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);

        findViewById(R.id.returnBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CostumerOrder.class));
            }
        });



        ccdialog= new Dialog(this);
        Bundle b = new Bundle(this.getIntent().getExtras());
        products = b.getParcelableArrayList("products");

        userN = findViewById(R.id.UserName);
        FinalPrice = findViewById(R.id.Price);

        location = findViewById(R.id.Location);
        phone = findViewById(R.id.PhoneNum);

        findViewById(R.id.PayCredit).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(checkData()) {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                    return;
                }
                payementmethod="CreditCard";
                ShowPopup(findViewById(R.id.PayCredit));
            }
        });

        findViewById(R.id.PayCash).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(checkData()) {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                    return;
                }
                payementmethod="cash";
                InsertOrderToDB();
                Toast.makeText(getApplicationContext(),"Your order on the way",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),CostumerOrder.class));
            }
        });

        User user = new User(MainActivity.getUser());

        f = 0;
        for (int i = 0; i < products.size(); i++){
            if(products.get(i).getQuantity().equals("0")) {
                products.remove(products.get(i));
                i--;
            }
            else
            f += (Float.parseFloat(products.get(i).getPrice()))*(Integer.parseInt(products.get(i).getQuantity()));
        }
        userN.setText(user.getName());
        FinalPrice.setText("Final price: "+f+"ILS");
    }

    public void ShowPopup(final View v)
    {
        ccdialog.setContentView(R.layout.credit_card_dialog);
        vid=(EditText) ccdialog.findViewById(R.id.txtccID);
        vccnum=(EditText) ccdialog.findViewById(R.id.txtccNumber);
        vmonth=(EditText) ccdialog.findViewById(R.id.txtccmonth);
        vyear=(EditText) ccdialog.findViewById(R.id.txtccyear);
        vcvv=(EditText) ccdialog.findViewById(R.id.txtCCcvv);

        final Button cancel,pay;
        pay=(Button) ccdialog.findViewById(R.id.btnccpay);
        cancel=(Button) ccdialog.findViewById(R.id.btnccCancel);
        ccdialog.show();
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtid=vid.getText().toString().trim();
                txtccnum=vccnum.getText().toString().trim();
                txtmonth=vmonth.getText().toString().trim();
                txtyear=vyear.getText().toString().trim();
                txtcvv=vcvv.getText().toString().trim();
                if(txtid.isEmpty())
                {
                    vid.setError("ID Number is required");
                    vid.requestFocus();
                    return;
                }
                if(!Pattern.compile("[0-9]*").matcher(txtid).matches())
                {
                    vid.setError("ID Number is invalid");
                    vid.requestFocus();
                    return;
                }
                if(txtid.length()!=9)
                {
                    vid.setError("ID Number is invalid");
                    vid.requestFocus();
                    return;
                }
                if(txtccnum.isEmpty())
                {
                    vccnum.setError("CreditCard Number is required");
                    vccnum.requestFocus();
                    return;
                }
                if(txtccnum.length()!=16)
                {
                    vccnum.setError("CreditCard Number is invalid");
                    vccnum.requestFocus();
                    return;
                }
                if(!Pattern.compile("[0-9]*").matcher(txtccnum).matches())
                {
                    vccnum.setError("CreditCard Number is invalid");
                    vccnum.requestFocus();
                    return;
                }
                if(txtyear.isEmpty())
                {
                    vyear.setError("Expiration Year is required");
                    vyear.requestFocus();
                    return;
                }
                if(txtyear.length()!=4)
                {
                    vyear.setError("Expiration Year is invalid");
                    vyear.requestFocus();
                    return;
                }
                if(!Pattern.compile("[0-9]*").matcher(txtyear).matches())
                {
                    vyear.setError("Expiration Year is invalid");
                    vyear.requestFocus();
                    return;
                }
                if(txtmonth.isEmpty())
                {
                    vmonth.setError("Expiration Month is required");
                    vmonth.requestFocus();
                    return;
                }
                if(txtmonth.length()!=2)
                {
                    vmonth.setError("Expiration Month is invalid");
                    vmonth.requestFocus();
                    return;
                }
                if(!Pattern.compile("[0-9]*").matcher(txtmonth).matches())
                {
                    vmonth.setError("Expiration Month is invalid");
                    vmonth.requestFocus();
                    return;
                }
                if(txtcvv.isEmpty())
                {
                    vcvv.setError("CVV is required");
                    vcvv.requestFocus();
                    return;
                }
                if(txtcvv.length()!=3)
                {
                    vcvv.setError("CVV is invalid");
                    vcvv.requestFocus();
                    return;
                }
                if(!Pattern.compile("[0-9]*").matcher(txtcvv).matches())
                {
                    vcvv.setError("CVV is invalid");
                    vcvv.requestFocus();
                    return;
                }
                InsertOrderToDB();
                Toast.makeText(getApplicationContext(),"Your order is on the way",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),CostumerOrder.class));
                ccdialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ccdialog.dismiss();
            }
        });
    }
    boolean checkData(){
        String l = location.getText().toString().trim();
        String p = phone.getText().toString().trim();
        if(l.isEmpty()) {
            location.setError("location not found");
            location.requestFocus();
            return true;
        }
        if(p.isEmpty() ||  (!Pattern.compile("[0][5][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]").matcher(p).matches())){
            phone.setError("Invalid Phone Number");
            phone.requestFocus();
            return true;
        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
     void InsertOrderToDB(){
        String location_ = location.getText().toString().trim();
        String name_ = userN.getText().toString().trim();
        LocalDateTime now = LocalDateTime.now();
        final Order order = new Order(products, location_,f+"", now, name_,payementmethod);
        String json = new Gson().toJson(order);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("UnTakenOrders");
        Random r =new Random();
        int d= r.nextInt();
        myRef.child(d+"").setValue(json);
        final ArrayList<Order> orders = new ArrayList<Order>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String o = dataSnapshot.getValue(String.class);
                    orders.add(new Gson().fromJson(o,Order.class));
                }
                System.out.println(orders.get(0).getCostumerName());
                System.out.println(orders.get(0).getLocation());
                System.out.println(orders.get(0).getPrice());
                System.out.println(orders.get(0).getProducts().get(0).getQuantity());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}