package com.example.market;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.market.classes.Order;
import com.example.market.classes.Product;
import com.example.market.classes.QuanProduct;
import com.example.market.classes.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MakeOrder extends AppCompatActivity {

    ArrayList<QuanProduct> products;

    TextView userN, FinalPrice;
    EditText location, phone;
    Button payCash, payCC, returnbtn;




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

                /**
                 *
                 * pop up
                 * pop up
                 *
                 */
                //todo pop up


                InsertOrderToDB();
                Toast.makeText(getApplicationContext(),"Your order on the way",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),CostumerOrder.class));
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
                InsertOrderToDB();
                Toast.makeText(getApplicationContext(),"Your order on the way",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),CostumerOrder.class));
            }
        });





        User user = new User(MainActivity.getUser());

        float f = 0;
        for (int i = 0; i < products.size(); i++){
            f += (Float.parseFloat(products.get(i).getPrice())*products.get(i).getQuantity());
        }

        userN.setText(user.getName());
        FinalPrice.setText("Final price: "+f+"ILS");




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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order( products, location_, now, name_);

        // todo order lal data beeez


    }


}