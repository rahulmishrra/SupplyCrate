package com.example.supplycrate1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DebugTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_test);
        getSupportActionBar().hide();
        SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_CUSTOMER);
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        String _custemail = userDetails.get(SessionManager.KEY_EMAIL);
        String _custname = userDetails.get(SessionManager.KEY_NAME);
        String _custpassword = userDetails.get(SessionManager.KEY_PASSWORD);
        String _storename = userDetails.get(SessionManager.KEY_SELECTSTORENAME);

        Button debugnbtn = findViewById(R.id.debugtestbtn);

        debugnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference db  = FirebaseDatabase.getInstance().getReference("Customers").child("mahesh").child("Locality");

                db.setValue("Bhayandar East");
            }
        });
    }
}