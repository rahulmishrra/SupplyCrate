package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private String _itemtotal,_deliveryfee,_address,phoneno;
    private Button takeawaybtn,orderbtn;
    private String _custloc;

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


        DatabaseReference data = FirebaseDatabase.getInstance().getReference("Customers").child(_custname);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                address.setText(snapshot.child("Location").getValue().toString());
                _custloc = snapshot.child("Location").getValue().toString();
                phoneno = snapshot.child("phoneno").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //
        DatabaseReference custdb = FirebaseDatabase.getInstance().getReference("Customers").child(_custname).child("orders");
        DatabaseReference cstdb = FirebaseDatabase.getInstance().getReference("Customers").child(_custname).child("Cart");
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
                bdr.child("orders").child(orderkey).child("phoneno").setValue(phoneno);
                bdr.child("orders").child(orderkey).child("itemTotal").setValue(_itemtotal);
                custdb.child(storename).child(orderkey).child("orderId").setValue(orderkey);
                cstdb.removeValue();
                Toast.makeText(getApplicationContext(),"Order Sent, Track your order in orders section",Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(),CustDashboard.class));
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

                            bdr.child("Queue").child(orderkey).child("token").setValue("1");
                            for(int i=0; i<productkeys.length;i++){
                                bdr.child("Queue").child(orderkey).child("products").child(String.valueOf(i)).child("Productkey").setValue(productkeys[i]);
                                bdr.child("Queue").child(orderkey).child("products").child(String.valueOf(i)).child("Quantity").setValue(Quantities[i]);
                            }
                            bdr.child("Queue").child(orderkey).child("customerName").setValue(_custname);
                            bdr.child("Queue").child(orderkey).child("ordertotal").setValue(_itemtotal);
                            bdr.child("Queue").child(orderkey).child("Date").setValue(_currentDate);
                            bdr.child("Queue").child(orderkey).child("Time").setValue(_currentTime);
                            bdr.child("Queue").child(orderkey).child("custEmail").setValue(_custemail);
                            bdr.child("Queue").child(orderkey).child("Address").setValue(_custloc);
                            bdr.child("Queue").child(orderkey).child("orderId").setValue(orderkey);
                            Toast.makeText(getApplicationContext(),"Check your token number in orders section",Toast.LENGTH_SHORT).show();
                        }
                    }

                    private void getToken() {
                        bdref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                long count = snapshot.getChildrenCount();
                                String tokennum = String.valueOf(count);

                                bdref.child(orderkey).child("token").setValue(tokennum);
                                for(int i=0; i<productkeys.length;i++){
                                    bdref.child(orderkey).child("products").child(String.valueOf(i)).child("Productkey").setValue(productkeys[i]);
                                    bdref.child(orderkey).child("products").child(String.valueOf(i)).child("Quantity").setValue(Quantities[i]);
                                }
                                bdref.child(orderkey).child("customerName").setValue(_custname);
                                bdref.child(orderkey).child("ordertotal").setValue(String.valueOf(total));
                                bdref.child(orderkey).child("Date").setValue(_currentDate);
                                bdref.child(orderkey).child("Time").setValue(_currentTime);
                                bdref.child(orderkey).child("custEmail").setValue(_custemail);
                                bdref.child(orderkey).child("Address").setValue(_custloc);
                                bdref.child(orderkey).child("orderId").setValue(orderkey);
                                bdref.child(orderkey).child("orderStatus").setValue("Pending");
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