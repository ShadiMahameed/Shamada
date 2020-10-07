package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.market.classes.CostumerOrderAdapter;
import com.example.market.classes.Driver;
import com.example.market.classes.DriverInventoryAdapter;
import com.example.market.classes.Inventory;
import com.example.market.classes.Order;
import com.example.market.classes.Product;
import com.example.market.classes.QuanProduct;
import com.example.market.classes.User;
import com.example.market.classes.driverOrdersAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.example.market.classes.Constants.ERROR_DIALOG_REQUEST;
import static com.example.market.classes.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.market.classes.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class DriverOrders extends AppCompatActivity {

    private static final String TAG = "DriverOrders";
    ArrayList<Order> orders = new ArrayList();
    ArrayList<String> nameInDB = new ArrayList<>();
    List<QuanProduct> quantproducts=new ArrayList<QuanProduct>();
    FirebaseDatabase database;
    DatabaseReference inventory,UntakenOrders,TakenOrders;
    //static User driver=MainActivity.getUser();
    ArrayList<QuanProduct> products = new ArrayList<QuanProduct>();
    Inventory inv;
    RecyclerView recyclerView;
    int index=0;
    private boolean mLocationPermissionGranted=false;
    Button btnsignoutdriver;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_orders);
        btnsignoutdriver=(Button) findViewById(R.id.btnSignoutDriver);
        btnsignoutdriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });




        recyclerView=(RecyclerView) findViewById(R.id.recyclerorderdriver);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkMapServices();

        database = FirebaseDatabase.getInstance();
        TakenOrders = database.getReference().child("TakenOrders");
        UntakenOrders=database.getReference().child("UnTakenOrders");

        inventory = database.getReference().child("Inventory").child(MainActivity.getUser().getName());

        Query getOrder = TakenOrders.child(MainActivity.getUser().getName());
        getOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Toast.makeText(getApplicationContext(),"Deliver order first",Toast.LENGTH_LONG).show();
                }
                else
                {

                    inventory.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            products.clear();
                            QuanProduct p;
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                p = dataSnapshot.getValue(QuanProduct.class);
                                products.add(p);
                            }
                            inv = new Inventory(products,MainActivity.getUser().getName());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    UntakenOrders.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            quantproducts.clear();
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Order o = new Gson().fromJson(dataSnapshot.getValue(String.class),Order.class);
                                quantproducts=o.getProducts();
                                boolean flag=true;
                                for(int i=0;i<quantproducts.size();i++)
                                {
                                    if(inv.getProducts().contains(quantproducts.get(i)))
                                        index=inv.getProducts().indexOf(quantproducts.get(i));
                                    if(Float.parseFloat(quantproducts.get(i).getQuantity())>Float.parseFloat(inv.getProducts().get(index).getQuantity()))
                                    {
                                        flag=false;
                                    }
                                }
                                if(flag==true)
                                {
                                    orders.add(o);
                                    nameInDB.add(dataSnapshot.getKey());
                                }
                            }

                            recyclerView.setAdapter(new driverOrdersAdapter(orders,nameInDB,getApplicationContext()));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.driver_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home_driver);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_inventory:
                        startActivity(new Intent(getApplicationContext(),DriverInventory.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_home_driver:
                        break;
                    case R.id.nav_map:
                        startActivity(new Intent(getApplicationContext(),DriverNavigation.class));
                        overridePendingTransition(0,0);
                        break;
                }

                return false;
            }
        });

    }
    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(DriverOrders.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(DriverOrders.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(mLocationPermissionGranted){
                    getLocationPermission();
                }

            }
        }

    }
}