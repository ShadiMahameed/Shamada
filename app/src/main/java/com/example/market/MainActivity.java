package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.market.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    String Name,Password,Email,Type;
    FirebaseAuth mAuth;
    EditText txtEmail,txtPassword;
    FirebaseDatabase database;
    DatabaseReference  myRef;
    static User user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        txtEmail=findViewById(R.id.txtEmaillogin);
        txtPassword=findViewById(R.id.txtPasslogin);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");
        findViewById(R.id.btnSignup1).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
    }
    private void userLogin()
    {
        Name = txtEmail.getText().toString().trim();
        Password=txtPassword.getText().toString().trim();
        if (Name.isEmpty()) {
            txtEmail.setError("Username is required");
            txtEmail.requestFocus();
            return;
        }
        if(!Pattern.compile("[0-9A-Za-z]*").matcher(Name).matches())
        {
            txtEmail.setError("Invalid username");
            txtEmail.requestFocus();
            return;
        }
       /* if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
        {
            txtEmail.setError("Email is not valid!");
            txtEmail.requestFocus();
            return;
        }*/
        if(Password.isEmpty())
        {
            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
            return;
        }
       // Query checkuser = myRef.orderByChild("name").equalTo(Name);
        Query checkuser = myRef.child(Name);
       checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists())
               {
                   user=snapshot.getValue(User.class);
                   /*Email=snapshot.child(Name).child("email").getValue(String.class);
                   System.out.println("@@@@@@@@@@@@@@@@@" + Email + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@");*/
                   Type=user.getType();
                   goLogin(user.getEmail(),Password);
               }
               else {
                   txtEmail.setError("Username Does not exist");
                   txtEmail.requestFocus();
                   return;
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    private void goLogin(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_LONG).show();
                    if ("Costumer".equals(Type)) {
                        startActivity(new Intent(getApplicationContext(), CostumerOrder.class));
                    }
                    else
                    if(Type.equals(("Driver")))
                        startActivity(new Intent(getApplicationContext(),DriverOrders.class));
                    else
                    if(Type.equals("Admin"))
                        startActivity(new Intent(getApplicationContext(),Admin.class));
                }
                else
                {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==(R.id.btnSignup1))
            startActivity(new Intent(getApplicationContext(),signup.class));
        else
            {
            if (v.getId() == (R.id.btnLogin))
                userLogin();
            }

    }

    public static User getUser() {
        return user;
    }
    public static void setUser(User u){
        user=new User(u);
    }
}
