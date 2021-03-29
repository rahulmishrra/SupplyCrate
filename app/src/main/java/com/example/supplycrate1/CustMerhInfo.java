package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CustMerhInfo extends AppCompatActivity {

    private TextView _custmerchnametitle;
    private ListView _custcategorylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_merh_info);
        getSupportActionBar().hide();

        _custmerchnametitle = findViewById(R.id.custmerchnametitle);
        _custcategorylist = findViewById(R.id.custcategorylist);

        String storetitle = getIntent().getStringExtra("StoreName");

        _custmerchnametitle.setText(storetitle);

        DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("Merchants").child(storetitle).child("Categories");
        List<String> custcategorieslist = new ArrayList<>();

        CategoryAdapter categoryAdapter = new CategoryAdapter(getApplicationContext(),custcategorieslist);

        dbrefer.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                custcategorieslist.add(snapshot.getValue(String.class));
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                custcategorieslist.remove(snapshot.getValue(String.class));
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(custcategorieslist.size() == 0){
            custcategorieslist.add("NO PRODUCTS OR CATEGORY FOUND");
            _custcategorylist.setAdapter(categoryAdapter);
        }
        else{
            _custcategorylist.setAdapter(categoryAdapter);
        }

        _custcategorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = custcategorieslist.get(position).toString();
                Toast.makeText(getApplicationContext(),category,Toast.LENGTH_SHORT).show();
                Intent intentdash = new Intent(getApplicationContext(),custProductpage.class);
                intentdash.putExtra("StoreName",storetitle);
                intentdash.putExtra("Category",category);
                startActivity(intentdash);
            }
        });



    }
}