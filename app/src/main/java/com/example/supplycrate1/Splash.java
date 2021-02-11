package com.example.supplycrate1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class Splash extends AppCompatActivity {


    private static int SPLASH_SCREEN = 4500;


    Animation topanime , bganime;
    ImageView logo, bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_splash);

        topanime = AnimationUtils.loadAnimation(this,R.anim.topanim);
        bganime = AnimationUtils.loadAnimation(this,R.anim.bganim);

        logo = findViewById(R.id.logoview);
        bg = findViewById(R.id.bgview);

        logo.setAnimation(topanime);
        bg.setAnimation(bganime);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the MainActivity. */
                Intent mainIntent = new Intent(Splash.this, RetailerDashboard.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_SCREEN);


    ;}
}