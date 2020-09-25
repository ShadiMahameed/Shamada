package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Admin extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Product> products;

    private TextView showprice;
    private TextView showname;
    private ImageView prodimage;
    private Button nextprod;
    private EditText prodname;
    private EditText prodprice;
    private ImageView newimage;
    private ImageButton addimage;
    private Button addprod;

    FirebaseDatabase database;
    DatabaseReference myRef;

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference ProductImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        showprice = findViewById(R.id.showPrice);
        showname = findViewById(R.id.showName);
        prodimage = findViewById(R.id.showImage);
        nextprod = findViewById(R.id.nextProduct);
        prodname = findViewById(R.id.ProductName);/////
        prodprice = findViewById(R.id.ProductPrice);////////////
        newimage = findViewById(R.id.newImage);

        findViewById(R.id.nextProduct).setOnClickListener(this);
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

        if(v.getId() == R.id.nextProduct){}

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


        UploadTask uploadTask = ProductImagesRef.putBytes(data);

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
                        Product p =new Product(prodname.getText().toString().trim(),prodprice.getText().toString().trim(),imageUrl);
                        myRef.push().setValue(p);
                    }
                });




            }
        });





    }



}