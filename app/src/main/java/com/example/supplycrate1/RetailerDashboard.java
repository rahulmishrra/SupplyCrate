package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RetailerDashboard extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_dashboard);

        bottomNavigationView = findViewById(R.id.merchantnavigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                Fragment temp =null;

                switch (item.getItemId())
                {
                    case R.id.merchantdashboard:temp = new merchantdashboard();
                    break;
                    case R.id.orders2:temp = new orders();
                    break;
                    case R.id.products:temp = new products();
                    break;
                    case R.id.queue:temp = new queue();
                    break;

                }


            }
        });

    }
}