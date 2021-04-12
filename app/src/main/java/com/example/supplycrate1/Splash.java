package com.example.supplycrate1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class Splash extends AppCompatActivity {


    private static int SPLASH_SCREEN = 4500;
    private boolean custlogin;
    private boolean mrchlogin;

    Animation topanime , bganime;
    ImageView logo, bg;

    @Override
    protected void onStart() {
        super.onStart();
        SessionManager custsession,mrchsession;
        custsession = new SessionManager(this,SessionManager.SESSION_CUSTOMER);
        mrchsession = new SessionManager(this,SessionManager.SESSION_MERCHANT);

        custlogin = custsession.checkLogin();
        mrchlogin = mrchsession.checkMerchantLogin();




    }

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
                    if(!isConnected(Splash.this)){
                        showCustomDialog();
                    }
                    else{

                        if(custlogin == true){
                            startActivity(new Intent(getApplicationContext(), CustomerDashboard.class));
                            finish();
                        }
                        else if(mrchlogin == true){
                            startActivity(new Intent(getApplicationContext(), RetailerDashboard.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }

                    /* Create an Intent that will start the MainActivity. */
                    //Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                    //startActivity(mainIntent);
                    //finish();
                }
            }

        }, SPLASH_SCREEN);


    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
        builder.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connnect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean isConnected(Splash splash) {
        ConnectivityManager connectivityManager = (ConnectivityManager) splash.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wificonn!=null && wificonn.isConnected())  || mobileconn != null && mobileconn.isConnected()){
            return true;
        }
        else{
            return false;
        }

    }

}