package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.market.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity implements View.OnClickListener
{
    EditText txtname , txtemail , txtpass,txtpass2;
    RadioGroup rgb;
    private FirebaseAuth mAuth;
    DatabaseReference reff;
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
        findViewById(R.id.btnSignup2).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }
    private void registerMember() {
        String Email = txtemail.getText().toString().trim();
        String Name = txtname.getText().toString().trim();
        String Pass1 = txtpass.getText().toString().trim();
        String passw2 = txtpass2.getText().toString().trim();

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

        if(Name.isEmpty())
        {
            txtname.setError("Name is required");
            txtname.requestFocus();
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

        mAuth.createUserWithEmailAndPassword(Email,Pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"User Registered Successfully",Toast.LENGTH_LONG).show();
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
    @Override
    public void onClick(View v) {
        if(v.getId()==(R.id.btnSignup2))
            registerMember();
    }
}