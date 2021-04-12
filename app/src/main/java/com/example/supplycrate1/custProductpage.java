package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class custProductpage extends AppCompatActivity {

    private TextView _custmerchnametitle;
    private ListView _custproductlist;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_productpage);
        getSupportActionBar().hide();

        _custproductlist = findViewById(R.id.custproductlist);
        toolbar = findViewById(R.id.custprdctoolbar);

        String storetitle = getIntent().getStringExtra("StoreName");
        String category = getIntent().getStringExtra("Category");
        toolbar.setTitle(category);

        SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_CUSTOMER);
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        String _custemail = userDetails.get(SessionManager.KEY_EMAIL);
        String _custpassword = userDetails.get(SessionManager.KEY_PASSWORD);
        String _custname = userDetails.get(SessionManager.KEY_NAME);
        //String storetitle = userDetails.get(SessionManager.KEY_SELECTSTORENAME);

        DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("Merchants").child(storetitle).child("Products");
        List<String> custproductslist,custunitlist,custpricelist,custcategorieslist,custprdctimageurl,custproductkey;

        custproductslist = new ArrayList<>();
        custunitlist = new ArrayList<>();
        custpricelist = new ArrayList<>();
        custcategorieslist = new ArrayList<>();
        custprdctimageurl = new ArrayList<>();
        custproductkey = new ArrayList<>();


        CustProductAdapter custProductAdapter = new CustProductAdapter(getApplicationContext(),custproductslist,custunitlist,custpricelist,custcategorieslist,custprdctimageurl,custproductkey,_custname);

        dbrefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String ctgry = dataSnapshot.getValue(ProductHelper.class).getProductCategory();
                    if (!ctgry.equals(category)){
                        continue;
                    }

                    custproductslist.add(dataSnapshot.getValue(ProductHelper.class).getProductName());
                    custunitlist.add(dataSnapshot.getValue(ProductHelper.class).getProductUnit());
                    custpricelist.add(dataSnapshot.getValue(ProductHelper.class).getSellingprice());
                    custprdctimageurl.add(dataSnapshot.getValue(ProductHelper.class).getProductImageUrl());
                    custproductkey.add(dataSnapshot.getValue(ProductHelper.class).getProductkey());
                    custcategorieslist.add(ctgry);

                }
                custProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        _custproductlist.setAdapter(custProductAdapter);

        _custproductlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*String _custproductslist = custproductslist.get(position).toString();
                String _custunitlist = custunitlist.get(position).toString();
                String _custpricelist = custpricelist.get(position).toString();
                String _custcategorieslist = custcategorieslist.get(position).toString();
                String _custprdctimageurl = custprdctimageurl.get(position).toString();
                */
                String _custproductkey = custproductkey.get(position).toString();
                Intent intentProductdetails = new Intent(getApplicationContext(),ProjectDetailsPage.class);
                intentProductdetails.putExtra("Productkey",_custproductkey);
                intentProductdetails.putExtra("StoreName",storetitle);
                startActivity(intentProductdetails);

                //Toast.makeText(getApplicationContext(),_custproductslist+" "+_custunitlist+" "+_custpricelist+" "+_custcategorieslist +" "+_custprdctimageurl+" "+_custproductkey,Toast.LENGTH_SHORT).show();


            }
        });

    }
}