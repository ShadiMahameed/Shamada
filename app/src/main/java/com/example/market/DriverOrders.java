package com.example.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    LocationManager locationManager;
    Geocoder dest,source;
    String currentLocation;
    Order order;
    double longitude,latitude;
    Address address,address2;
    ProgressBar bar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_orders);
        bar = findViewById(R.id.progressBar_driver_orders);
        bar.setVisibility(View.VISIBLE);
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
                            orders.clear();
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

                            //orders = sort(orders);
                            if(orders.size()>0) {
                                bar.setVisibility(View.GONE);
                                recyclerView.setAdapter(new driverOrdersAdapter(sort(orders), nameInDB, getApplicationContext()));
                            }
                            else
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



    public double getDistance(String location)
    {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return 0;
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude=location.getLatitude();
                    longitude=location.getLongitude();
                    LatLng latLng=new LatLng(latitude,longitude);
                    source = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressListsrs=source.getFromLocation(latitude,longitude,1);
                        currentLocation=addressListsrs.get(0).getLocality()+','+addressListsrs.get(0).getCountryName();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
                @Override
                public void onProviderEnabled(String provider) {
                }
                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
        else
        {
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double latitude=location.getLatitude();
                        double longtitude=location.getLongitude();
                        LatLng latLng=new LatLng(latitude,longtitude);
                        source = new Geocoder(getApplicationContext());
                        try {
                            List<Address> addressListsrs=source.getFromLocation(latitude,longtitude,1);
                            currentLocation=addressListsrs.get(0).getLocality()+','+addressListsrs.get(0).getCountryName();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }
        }
        //String location2 = "Haifa University";
        List<Address> addressListdest = null;

        if (location != null || !location.equals("")) {
            dest = new Geocoder(DriverOrders.this);
            try {
                addressListdest = dest.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            address = addressListdest.get(0);
            // LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        }

        double longDiff=longitude-address.getLongitude();
        double distance=((Math.sin(deg2rad(latitude)))   * (Math.sin(deg2rad(address.getLatitude())))
                +(Math.cos(deg2rad(latitude)))   * (Math.cos(deg2rad(address.getLatitude()))))
                *Math.cos(deg2rad(longDiff));
        distance=Math.acos(distance);
        distance=rad2deg(distance);
        distance=distance*60*1.1515;
        distance=distance*1.609344;

        return distance;
    }
    private double deg2rad(double lat1)
    {
        return (lat1*Math.PI/180.0);
    }
    private double rad2deg(double distance)
    {
        return (distance * 180.0/Math.PI);
    }



    public ArrayList<Order> sort(ArrayList<Order> orders){
        ArrayList<Order> newOrders=new ArrayList<Order>();
        int min=0;
        double dist=getDistance(orders.get(0).getLocation());
        while(!orders.isEmpty()) {
            min=0;
            dist=getDistance(orders.get(0).getLocation());
            for (int i = 0; i < orders.size(); i++) {

                System.out.println(orders.get(i).getLocation()+ dist);

                if (getDistance(orders.get(i).getLocation()) > dist) {
                    dist = getDistance(orders.get(i).getLocation());
                    min = i;
                }
            }
            newOrders.add(orders.get(min));
            orders.remove(min);
        }


        return newOrders;
    }



}