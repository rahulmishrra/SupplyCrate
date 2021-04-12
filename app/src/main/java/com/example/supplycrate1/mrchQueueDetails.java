package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.ViewGroup;
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

public class mrchQueueDetails extends AppCompatActivity {

    private TextView date,orderStatus,name,email,address,phone;
    private ListView productlistdetails;
    long count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrch_queue_details);
        getSupportActionBar().hide();

        Toolbar mrchorderdetailtool = findViewById(R.id.mrchqueuedetailtoolbar);
        String orderid = getIntent().getStringExtra("OrderId");

        SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_MERCHANT);
        HashMap<String,String> mrchDetails = sessionManager.getMerchantDetailFromSession();
        String mbname = mrchDetails.get(SessionManager.KEY_MERCHANTBNAME);

        date = findViewById(R.id.queuedatedetail);
        name = findViewById(R.id.custnamequeuedetail);
        email = findViewById(R.id.custemailqueuedetails);
        address = findViewById(R.id.custaddqueuedetails);
        productlistdetails = findViewById(R.id.qprdctlistdetails);

        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Merchants").child(mbname).child("Queue").child(orderid);
        DatabaseReference dtbr = FirebaseDatabase.getInstance().getReference("Merchants").child(mbname).child("Queue").child(orderid).child("products");

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                date.setText(snapshot.child("Date").getValue(String.class));

                name.setText(snapshot.child("customerName").getValue(String.class));
                email.setText(snapshot.child("custEmail").getValue(String.class));
                address.setText(snapshot.child("Address").getValue(String.class));
                count = snapshot.child("products").getChildrenCount();
                setHeight(count);
                Toast.makeText(getApplicationContext(),String.valueOf(count),Toast.LENGTH_SHORT).show();
            }

            private void setHeight(long count) {
                ViewGroup.LayoutParams list = productlistdetails.getLayoutParams();
                list.height = (int) (350*count);
                productlistdetails.setLayoutParams(list);
                productlistdetails.requestLayout();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        List<String> productkey = new ArrayList<>();
        List<String> productqnty = new ArrayList<>();

        orderDetailsAdapter adapter = new orderDetailsAdapter(getApplicationContext(),productqnty,productkey,mbname);

        dtbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                productkey.add(snapshot.child("Productkey").getValue(String.class));
                productqnty.add(snapshot.child("Quantity").getValue(String.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                productkey.remove(snapshot.child("Productkey").getValue(String.class));
                productqnty.remove(snapshot.child("Quantity").getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        productlistdetails.setAdapter(adapter);


    }
}