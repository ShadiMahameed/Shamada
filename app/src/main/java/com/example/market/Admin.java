package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.market.classes.Product;
import com.example.market.classes.adminRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;


public class Admin extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Product> products = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;


    private EditText prodname;
    private EditText prodprice;
    private ImageView newimage;
    private ImageButton addimage;
    private Button addprod;

    FirebaseDatabase database;
    DatabaseReference myRef;

    FirebaseStorage storage;
    StorageReference ProductImagesRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        recyclerView = findViewById(R.id.recyclerAdmin);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Product p1 = new Product("haha","7.2","https://firebasestorage.googleapis.com/v0/b/market-4a476.appspot.com/o/images%2F0.4230189277096864?alt=media&token=96088b53-e1ee-418f-aaa4-abc600d18d7c");
        Product p2 = new Product("hahaha","71.2","https://firebasestorage.googleapis.com/v0/b/market-4a476.appspot.com/o/images%2F0.1189654691559695?alt=media&token=0f17c4ed-0b68-4bcd-943f-7bb465177679");
        products.add(p1);
        products.add(p2);

        adapter = new adminRecyclerAdapter(this, products);
        recyclerView.setAdapter(adapter);


        prodname = findViewById(R.id.ProductName);/////
        prodprice = findViewById(R.id.ProductPrice);////////////
        newimage = findViewById(R.id.newImage);


        findViewById(R.id.AddPic).setOnClickListener(this);
        findViewById(R.id.addProduct).setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Product");

        storage = FirebaseStorage.getInstance();
        ProductImagesRef = storage.getReference().child("images");




    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.addProduct)
            AddNewProduct();

        if(v.getId() == R.id.AddPic){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }


    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                newimage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }


    private void AddNewProduct(){
        BitmapDrawable drawable = (BitmapDrawable) newimage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        Random r =new Random();
        UploadTask uploadTask = ProductImagesRef.child(r.nextDouble()+"").putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();
                        String name=prodname.getText().toString().trim();
                        String price=prodprice.getText().toString().trim();
                        if(name.isEmpty() || price.isEmpty() || (!Pattern.compile("[0-9]*\\.?[0-9]+").matcher(price).matches())){
                            Toast.makeText(getApplicationContext(),"Wrong Input",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Product p =new Product(name,price,imageUrl);
                            myRef.child(name).setValue(p);
                            Toast.makeText(getApplicationContext(),"Product Added",Toast.LENGTH_LONG).show();
                            prodname.setText("");
                            prodprice.setText("");
                        }
                    }
                });




            }
        });





    }



}