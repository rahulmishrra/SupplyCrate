package com.example.supplycrate1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link custlocation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class custlocation extends Fragment  implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private boolean isPermissionGranted;
    private GoogleMap gMap;
    private FloatingActionButton fab;
    private Button setlocbtn;
    private FusedLocationProviderClient mLocationClient;
    private int GPS_REQUEST_CODE = 9001;
    private EditText locSearch;
    private ImageView searchIcon;
    private ListView merchListview;
    private String finaladrress,finalpostalcode,finallocality;
    private Double finallat,finallong;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public custlocation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment custlocation.
     */
    // TODO: Rename and change types and number of parameters
    public static custlocation newInstance(String param1, String param2) {
        custlocation fragment = new custlocation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custlocation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setlocbtn = getView().findViewById(R.id.setcustloc);
        fab = getView().findViewById(R.id.custfab);
        locSearch = getView().findViewById(R.id.custet_search);
        searchIcon = getView().findViewById(R.id.custsearch_icon);
        merchListview = getView().findViewById(R.id.merchantlistview);

        SessionManager sessionManager = new SessionManager(getContext(),SessionManager.SESSION_CUSTOMER);
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        String _custemail = userDetails.get(SessionManager.KEY_EMAIL);
        String _custpassword = userDetails.get(SessionManager.KEY_PASSWORD);
        String _custname = userDetails.get(SessionManager.KEY_NAME);
        String _custloc = userDetails.get(SessionManager.KEY_LOCATION);

        List<String> merchnamelist = new ArrayList<>();
        List<String> merchImglist = new ArrayList<>();

        MerchantAdapter merchantAdapter = new MerchantAdapter(getContext(),merchnamelist,merchImglist);


        checkMyPermission();

        initMap();


        mLocationClient = new FusedLocationProviderClient(getContext());

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference db = firebaseDatabase.getReference("Merchants");
        DatabaseReference dbr  = firebaseDatabase.getReference("Customers");

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String locality = snapshot.child("Locality").getValue().toString();
                if(locality.equals(_custloc)){
                    merchnamelist.add(snapshot.getValue(BusinessHelperClass.class).getbName());
                    merchImglist.add(snapshot.child("merchantImageUrl").getValue().toString());
                }

                merchantAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                merchantAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                merchnamelist.remove(snapshot.getValue(BusinessHelperClass.class).getbName());
                merchImglist.remove(snapshot.child("merchantImageUrl").getValue().toString());
                merchantAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        merchListview.setAdapter(merchantAdapter);

        merchListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String businessName = merchnamelist.get(position).toString();
                sessionManager.custSelectStore(businessName);
            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrLoc();

            }
        });
        DatabaseReference dtbr = FirebaseDatabase.getInstance().getReference("Customers").child(_custname);
        setlocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finaladrress == null){
                    Toast.makeText(getContext(),"please select your address..",Toast.LENGTH_SHORT).show();
                }
                else{
                    dtbr.child("Location").setValue(finaladrress);
                    dtbr.child("Latitude").setValue(finallat);
                    dtbr.child("Longitude").setValue(finallong);
                    dtbr.child("Locality").setValue(finallocality);
                    dtbr.child("PostalCode").setValue(finalpostalcode);
                    SessionManager sessionManager = new SessionManager(getContext(),SessionManager.SESSION_CUSTOMER);
                    sessionManager.setLocation(finallocality);
                }
            }
        });

        searchIcon.setOnClickListener(this::geoLocate);

    }

    private void addMerchants() {
    }

    private void geoLocate(View view) {
        String locationName = locSearch.getText().toString();

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);

            if (addressList.size() > 0) {
                Address address = addressList.get(0);

                gotoLocation(address.getLatitude(), address.getLongitude());

                gMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())));

                finaladrress = address.getAddressLine(0);
                finallat = address.getLatitude();
                finallong = address.getLongitude();
                finallocality = address.getSubLocality();
                finalpostalcode = address.getPostalCode();

                Toast.makeText(getContext(), address.getAddressLine(0), Toast.LENGTH_SHORT).show();
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

                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);

                gotoLocation(location.getLatitude(), location.getLongitude());
                finaladrress = cityName;
                finallat = location.getLatitude();
                finallong = location.getLongitude();
                finallocality = addresses.get(0).getSubLocality();
                finalpostalcode = addresses.get(0).getPostalCode();
                //Toast.makeText(getApplicationContext(),cityName ,Toast.LENGTH_SHORT).show();

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

        Toast.makeText(getContext(), latLng.toString(), Toast.LENGTH_SHORT).show();
    }

    private void initMap() {
        if (isPermissionGranted) {

            SupportMapFragment supportMapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.custgooglemap);

            supportMapFragment = SupportMapFragment.newInstance();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.custgooglemap, supportMapFragment)
                    .commit();

            supportMapFragment.getMapAsync(this);


        }
    }



    private void checkMyPermission() {
        Dexter.withContext(getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGranted = true;
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                //Uri uri = Uri.fromParts("package", getPackageName(), "");
                //intent.setData(uri);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == GPS_REQUEST_CODE) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean provideEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (provideEnable) {
                Toast.makeText(getContext(), "GPS is enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "GPS is not enabled", Toast.LENGTH_SHORT).show();
            }
        }*/


    }
}