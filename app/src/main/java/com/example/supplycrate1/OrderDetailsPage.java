package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class OrderDetailsPage extends AppCompatActivity {

    private TextView itemtotal,ordertotal,address;
    private String _itemtotal,_deliveryfee,_address;
    private Button takeawaybtn,orderbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_page);
        getSupportActionBar().hide();

        itemtotal = findViewById(R.id.orderitemtotal);
        ordertotal = findViewById(R.id.ordertotal);
        address = findViewById(R.id.custaddress);
        takeawaybtn = findViewById(R.id.takeawaybtn);
        orderbtn = findViewById(R.id.orderbtn);

        _itemtotal = getIntent().getStringExtra("Item total");
        String storename = getIntent().getStringExtra("StoreName");
        String[] productkeys = getIntent().getStringArrayExtra("Productkey");
        String[] Quantities = getIntent().getStringArrayExtra("Quantity");

        int total = Integer.valueOf(_itemtotal) + 50;
        itemtotal.setText("\u20B9"+_itemtotal);
        ordertotal.setText("\u20B9"+String.valueOf(total));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate  = new SimpleDateFormat("MMM dd,yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");

        SimpleDateFormat orderKeyGenerator = new SimpleDateFormat("yyMMddHHmmss");
        String _currentDate = currentDate.format(calendar.getTime());
        String _currentTime = currentTime.format(calendar.getTime());

        String orderkey = orderKeyGenerator.format(calendar.getTime());

        SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_CUSTOMER);
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        String _custemail = userDetails.get(SessionManager.KEY_EMAIL);
        String _custpassword = userDetails.get(SessionManager.KEY_PASSWORD);
        String _custname = userDetails.get(SessionManager.KEY_NAME);

        address.setText(_custloc);

        DatabaseReference bdr = FirebaseDatabase.getInstance().getReference("Merchants").child(storename);
        DatabaseReference bdref = FirebaseDatabase.getInstance().getReference("Merchants").child(storename).child("Queue");

        orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<productkeys.length;i++){
                    bdr.child("orders").child(orderkey).child("products").child(String.valueOf(i)).child("Productkey").setValue(productkeys[i]);
                    bdr.child("orders").child(orderkey).child("products").child(String.valueOf(i)).child("Quantity").setValue(Quantities[i]);
                }
                bdr.child("orders").child(orderkey).child("customerName").setValue(_custname);
                bdr.child("orders").child(orderkey).child("ordertotal").setValue(String.valueOf(total));
                bdr.child("orders").child(orderkey).child("Date").setValue(_currentDate);
                bdr.child("orders").child(orderkey).child("Time").setValue(_currentTime);
                bdr.child("orders").child(orderkey).child("custEmail").setValue(_custemail);
                bdr.child("orders").child(orderkey).child("Address").setValue(_custloc);
                bdr.child("orders").child(orderkey).child("orderId").setValue(orderkey);
                bdr.child("orders").child(orderkey).child("orderStatus").setValue("Pending");

                Toast.makeText(getApplicationContext(),"Order Sent",Toast.LENGTH_SHORT).show();
            }
        });

        takeawaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bdr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("Queue")){
                            getToken();
                        }
                        else{
                            bdr.child("Queue").child(orderkey).child("customer").setValue(_custname);
                            bdr.child("Queue").child(orderkey).child("token").setValue("1");
                        }
                    }

                    private void getToken() {
                        bdref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                long count = snapshot.getChildrenCount();
                                String tokennum = String.valueOf(count);

                                bdref.child(orderkey).child("customer").setValue(_custname);
                                bdref.child(orderkey).child("token").setValue(tokennum);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}