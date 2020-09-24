package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    FirebaseAuth mAuth;
    EditText txtEmail,txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        txtEmail=findViewById(R.id.txtEmaillogin);
        txtPassword=findViewById(R.id.txtPasslogin);
        findViewById(R.id.btnSignup1).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("hamada 6eze");

    }
    private void userLogin()
    {
        String Email = txtEmail.getText().toString().trim();
        String Password=txtPassword.getText().toString().trim();
        if (Email.isEmpty()) {
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
        {
            txtEmail.setError("Email is not valid!");
            txtEmail.requestFocus();
            return;
        }
        if(Password.isEmpty())
        {
            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
            return;
        }
       mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful())
               {
                   Toast.makeText(getApplicationContext(),"You're Logged in",Toast.LENGTH_LONG).show();
                   startActivity(new Intent(getApplicationContext(),CostumerHome.class));

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


}
