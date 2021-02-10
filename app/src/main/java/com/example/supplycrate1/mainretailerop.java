package com.example.supplycrate1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class mainretailerop extends AppCompatActivity {


    Button signin;
    TextView signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mainretailerop);

        signin = findViewById(R.id.BTN1);
        signup = findViewById(R.id.TXT1);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(com.example.supplycrate1.mainretailerop.this, mainretailer2op.class);
                startActivity(intent);
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(com.example.supplycrate1.mainretailerop.this, mainretailer3op.class);
                startActivity(intent);
            }
        });



    }
}