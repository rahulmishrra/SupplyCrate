package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerDashboard extends AppCompatActivity {

    BottomNavigationView custnavigationview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customer_dashboard);


        custnavigationview = findViewById(R.id.customernavigation);
        custnavigationview.setSelectedItemId(R.id.custlocation);

        getSupportFragmentManager().beginTransaction().replace(R.id.custlocation,new CustDashboard()).commit();

        custnavigationview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment temp =null;

                switch (item.getItemId())
                {
                    case R.id.custDashboard:
                        temp = new CustDashboard();
                        Log.i("Acivity name", "***********************************************************************************************Cust Dashbord");
                        break;
                    case R.id.custQueues:
                        temp = new CustQueues();
                        Log.i("Acivity name", " Cust Queues");
                        break;
                    case R.id.custlocation:
                        temp = new custlocation();
                        Log.i("Acivity name", "custlocation ");
                        break;
                    case R.id.custorders:
                        temp = new custorders();
                        Log.i("Acivity name", "custorders ");
                        break;
                    case R.id.custSettings:
                        temp = new CustSettings();
                        Log.i("Acivity name", "Cust Settings ");
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.custFrame,temp).commit();


                return true;
            }
        });

        custnavigationview.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                Fragment temp =null;

                switch (item.getItemId())
                {
                    case R.id.custDashboard:
                        temp = new CustDashboard();
                        break;
                    case R.id.custQueues:
                        temp = new CustQueues();
                        break;
                    case R.id.custlocation:
                        temp = new custlocation();
                        break;
                    case R.id.custorders:
                        temp = new custorders();
                        break;
                    case R.id.custSettings:
                        temp = new CustSettings();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.custFrame,temp).commit();


            }
        });

    }
}