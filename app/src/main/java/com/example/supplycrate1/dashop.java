package com.example.supplycrate1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class dashop extends AppCompatActivity {
    ImageView cart;
    Button create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashop);
        getSupportActionBar().hide(); // hide the title bar


        cart = findViewById(R.id.imageView9);
        create = findViewById(R.id.floatbtn);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashop.this, createcrate.class);
                startActivity(intent);

            }
        });



    }
}