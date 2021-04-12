package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.Console;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLEngineResult;

public class MerchLocation extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private boolean isPermissionGranted;
    private GoogleMap gMap;
    private FloatingActionButton fab;
    private Button continuebtn;
    private FusedLocationProviderClient mLocationClient;
    private int GPS_REQUEST_CODE = 9001;
    private EditText locSearch;
    private ImageView searchIcon;
    private String finaladrress,finalpostalcode,finallocality;
    private Double finallat,finallong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merch_location);
        getSupportActionBar().hide();

        fab = findViewById(R.id.fab);
        locSearch = findViewById(R.id.et_search);
        searchIcon = findViewById(R.id.search_icon);
        continuebtn = findViewById(R.id.merchlocbtn);
        SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_MERCHANT);
        HashMap<String,String> mrchDetails = sessionManager.getMerchantDetailFromSession();

        String mbname = mrchDetails.get(SessionManager.KEY_MERCHANTBNAME);



        checkMyPermission();

        initMap();


        mLocationClient = new FusedLocationProviderClient(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrLoc();
            }
        });

        searchIcon.setOnClickListener(this::geoLocate);

        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(finaladrress==null){
                    Toast.makeText(getApplicationContext(),"Please set your Location",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),finaladrress+mbname,Toast.LENGTH_SHORT).show();
                    SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_MERCHANT);
                    sessionManager.setMerchantLocation(finaladrress);

                    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Merchants").child(mbname);

                    dbr.child("Location").setValue(finaladrress);
                    dbr.child("Latitude").setValue(finallat);
                    dbr.child("Longitude").setValue(finallong);
                    dbr.child("Locality").setValue(finallocality);
                    dbr.child("PostalCode").setValue(finalpostalcode);
                    startActivity(new Intent(getApplicationContext(),RetailerDashboard.class));
                }

            }
        });
    }

    private void geoLocate(View view) {
        String locationName = locSearch.getText().toString();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);

            if (addressList.size() > 0) {
                Address address = addressList.get(0);

                gotoLocation(address.getLatitude(), address.getLongitude());

                gMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())));

                finaladrress = address.getAddressLine(0);
                finallat = address.getLatitude();
                finallong = address.getLongitude();
                finallocality = address.getLocality();
                finalpostalcode = address.getPostalCode();

                //Toast.makeText(this, finaladrress,Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrLoc() {
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                String moreadd = addresses.get(0).getSubAdminArea();

                gotoLocation(location.getLatitude(), location.getLongitude());
                finaladrress = cityName;
                finallat = location.getLatitude();
                finallong = location.getLongitude();
                finallocality = addresses.get(0).getLocality();
                finalpostalcode = addresses.get(0).getPostalCode();

                Toast.makeText(getApplicationContext(),moreadd,Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        gMap.moveCamera(cameraUpdate);
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        gMap.addMarker(markerOptions);

        Toast.makeText(this, latLng.toString(), Toast.LENGTH_SHORT).show();
    }

    private void initMap() {
        if (isPermissionGranted) {

                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.merchgooglemap);

                supportMapFragment = SupportMapFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.merchgooglemap, supportMapFragment)
                        .commit();

                supportMapFragment.getMapAsync(this);


        }
    }



    private void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGranted = true;
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_REQUEST_CODE) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean provideEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (provideEnable) {
                Toast.makeText(this, "GPS is enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "GPS is not enabled", Toast.LENGTH_SHORT).show();
            }
        }


    }
}