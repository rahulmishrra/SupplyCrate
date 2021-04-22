package com.example.supplycrate1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button retailSignup,custSignup;
    ImageView top, logo;
    Animation topanim,zoom;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_main);


        retailSignup=findViewById(R.id.button);
        custSignup = findViewById(R.id.button3);
        top=findViewById(R.id.imageView);
        logo=findViewById(R.id.imageView2);
        topanim = AnimationUtils.loadAnimation(this,R.anim.topdown);
        zoom = AnimationUtils.loadAnimation(this,R.anim.bganim);




        top.setAnimation(topanim);
        logo.setAnimation(topanim);




        custSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    view.getContext().startActivity(intent);
                }


        });

        retailSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), mainretailer2op.class);
                startActivity(intent);
            }
        });



    }



}