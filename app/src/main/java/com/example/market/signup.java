package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.market.R;
import com.example.market.classes.DriverInventoryAdapter;
import com.example.market.classes.Product;
import com.example.market.classes.QuanProduct;
import com.example.market.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity implements View.OnClickListener
{
    String Email,Name,Pass1,passw2,phone;
    EditText txtname , txtemail , txtpass , txtpass2 , txtphone;
    ImageButton btnHome;
    RadioGroup rgb;
    User user;
    RadioButton radioButton;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef,myRefinv,myRefpro;
    Map<String, Object> driverinv;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // t3refat
        txtname=(EditText)findViewById(R.id.txtName);
        txtemail=(EditText)findViewById(R.id.txtEmailsign);
        txtpass=(EditText)findViewById(R.id.txtPasssign);
        txtpass2=(EditText)findViewById(R.id.txtPasssign2);
        txtphone=(EditText)findViewById(R.id.txtSignPhone);
        rgb=(RadioGroup)findViewById(R.id.radioGroup2);
        findViewById(R.id.btnBackHome).setOnClickListener(this);
        findViewById(R.id.btnSignup2).setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("User");
        myRefpro=database.getReference("Product");
        myRefinv = database.getReference().child("Inventory");
        driverinv = new HashMap<>();
        mAuth = FirebaseAuth.getInstance();

    }
    private void registerMember() {
         Email = txtemail.getText().toString().trim();
         Name = txtname.getText().toString().trim();
         Pass1 = txtpass.getText().toString().trim();
         passw2 = txtpass2.getText().toString().trim();
         phone=txtphone.getText().toString().trim();


        if(Name.isEmpty())
        {
            txtname.setError("Name is required");
            txtname.requestFocus();
            return;
        }
        if(!Pattern.compile("[0-9A-Za-z]*").matcher(Name).matches())
        {
            txtname.setError("Invalid username, Can only contain letters and numbers");
            txtname.requestFocus();
            return;
        }

        Query checkuser = myRef.orderByChild("name").equalTo(Name);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    if (Email.isEmpty()) {
                        txtemail.setError("Email is required");
                        txtemail.requestFocus();
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                    {
                        txtemail.setError("Email is not valid!");
                        txtemail.requestFocus();
                        return;
                    }
                    if(Pass1.isEmpty())
                    {
                        txtpass.setError("Password is required");
                        txtpass.requestFocus();
                        return;
                    }
                    if(passw2.isEmpty())
                    {
                        txtpass2.setError("Please Confirm Password");
                        txtpass2.requestFocus();
                        return;
                    }
                    if(!Pass1.equals(passw2))
                    {
                        txtpass2.setError("Password does not match!");
                        txtpass2.requestFocus();
                        return;
                    }
                    if(rgb.getCheckedRadioButtonId()==-1)
                    {
                        Toast.makeText(getApplicationContext(), "Please select how you would like to use the app", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        radioButton=(RadioButton) findViewById(rgb.getCheckedRadioButtonId());
                    }

                    if(phone.isEmpty())
                    {
                        txtphone.setError("Phone is required");
                        txtphone.requestFocus();
                        return;
                    }

                    if(!Pattern.compile("[0][5][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]").matcher(phone).matches()){
                        txtphone.setError("Invalid Phone Number");
                        txtphone.requestFocus();
                        return;
                    }

                    mAuth.createUserWithEmailAndPassword(Email,Pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"User Registered Successfully",Toast.LENGTH_LONG).show();
                                user=new User(Name,Email,radioButton.getText().toString(),phone);
                                myRef.child(Name).setValue(user);

                                if ("Costumer".equals(user.getType())) {
                                    startActivity(new Intent(getApplicationContext(), CostumerOrder.class));
                                }
                                else
                                if(user.getType().equals(("Driver"))) {
                                    myRefpro.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot dataSnapshot : snapshot.getChildren())
                                            {
                                                product=dataSnapshot.getValue(Product.class);
                                                driverinv.put(product.getName(),new QuanProduct(product,0) );
                                            }
                                            myRefinv.child(Name).updateChildren(driverinv);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    startActivity(new Intent(getApplicationContext(), DriverOrders.class));

                                }
                            }
                            else
                            {
                                if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                    Toast.makeText(getApplicationContext(),"Email Already Registered",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else
                if(snapshot.exists())
                {
                    txtname.setError("Username Taken , please try something else");
                    txtname.requestFocus();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
    @Override
    public void onClick(View v) {
        if(v.getId()==(R.id.btnSignup2))
            registerMember();
        if(v.getId()==(R.id.btnBackHome))
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }
    }
}