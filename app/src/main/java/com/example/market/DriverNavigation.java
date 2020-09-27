package com.example.market;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Locale;

public class DriverNavigation extends FragmentActivity implements OnMapReadyCallback {

    String currentLocation;
    GoogleMap googleMaps;
    SupportMapFragment mapFragment;
    SearchView searchView;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    Geocoder dest,source;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_navigation);

        searchView = findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude=location.getLatitude();
                    double longtitude=location.getLongitude();
                    LatLng latLng=new LatLng(latitude,longtitude);
                    source = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressListsrs=source.getFromLocation(latitude,longtitude,1);
                        currentLocation=addressListsrs.get(0).getLocality()+','+addressListsrs.get(0).getCountryName();
                        googleMaps.addMarker(new MarkerOptions().position(latLng).title(currentLocation));
                        googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
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
                            googleMaps.addMarker(new MarkerOptions().position(latLng).title(currentLocation));
                            googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressListdest = null;

                if (location != null || !location.equals("")) {
                    dest = new Geocoder(DriverNavigation.this);
                    try {
                        addressListdest = dest.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressListdest.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    googleMaps.addMarker(new MarkerOptions().position(latLng).title(location));
                    googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    if(!currentLocation.equals("")&&!location.equals(""))
                    {
                        DisplayTrack(currentLocation,location);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.driver_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_map);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_inventory:
                        startActivity(new Intent(getApplicationContext(), DriverInventory.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.nav_home_driver:
                        startActivity(new Intent(getApplicationContext(), DriverOrders.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.nav_map:
                        break;
                }

                return false;
            }
        });
        return;
    }

    private void DisplayTrack(String currentLocation, String location) {
         try {
             Uri uri = Uri.parse("https://www.google.co.in/maps/dir/"+currentLocation+"/"+location);
             Intent intent=new Intent(Intent.ACTION_VIEW,uri);
             intent.setPackage("com.google.android.apps.maps");
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(intent);
         }catch (ActivityNotFoundException e){
             Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
             Intent intent=new Intent(Intent.ACTION_VIEW,uri);
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(intent);
         }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMaps=googleMap;

    }
}