package com.example.supplycrate1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

public class opop extends AppCompatActivity {

    private ImageView cart;
    private FloatingActionButton create;
    TextView sessiontext;
    Button custlogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opop);

        getSupportActionBar().hide(); // hide the title bar

        custlogout = findViewById(R.id.btnnnoon);
        sessiontext = findViewById(R.id.sessiontext);
        create = findViewById(R.id.floatbtn);

        SessionManager sessionManager = new SessionManager(this,SessionManager.SESSION_CUSTOMER);
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        String _custemail = userDetails.get(SessionManager.KEY_EMAIL);
        String _custpassword = userDetails.get(SessionManager.KEY_PASSWORD);

        sessiontext.setText(_custemail + "\n" + _custpassword);

        custlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUserFromSession();
                startActivity(new Intent(getApplicationContext(), Login .class));
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(opop.this, crate.class);
                //startActivity(intent);

            }
        });
    }
}