package com.example.supplycrate1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class opop extends AppCompatActivity {

    private ImageView cart;
    private FloatingActionButton create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opop);

        getSupportActionBar().hide(); // hide the title bar



        create = findViewById(R.id.floatbtn);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(opop.this, crate.class);
                startActivity(intent);

            }
        });
    }
}