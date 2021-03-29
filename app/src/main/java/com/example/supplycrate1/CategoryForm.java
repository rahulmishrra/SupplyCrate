package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CategoryForm extends AppCompatActivity {
    EditText ctgryname;
    Button catergstbtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbref,databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_form);
        getSupportActionBar().hide();

        ctgryname = findViewById(R.id.categoryname);
        catergstbtn = findViewById(R.id.cateformbtn);

        SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_MERCHANT);
        HashMap<String,String> mrchDetails = sessionManager.getMerchantDetailFromSession();
        String mbname = mrchDetails.get(SessionManager.KEY_MERCHANTBNAME);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate  = new SimpleDateFormat("MMM dd,yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String _currentDate = currentDate.format(calendar.getTime());
        String _currentTime = currentTime.format(calendar.getTime());

        String ctgrykey = _currentDate +" "+ _currentTime;


        catergstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryname = ctgryname.getText().toString();

                if(TextUtils.isEmpty(categoryname)){
                    ctgryname.setError("Category name is mandatory...");
                }

                firebaseDatabase = FirebaseDatabase.getInstance();
                dbref = firebaseDatabase.getReference("Merchants").child(mbname).child("Categories");

                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dbref.child(ctgrykey).setValue(categoryname);
                        startActivity(new Intent(getApplicationContext(),products.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}