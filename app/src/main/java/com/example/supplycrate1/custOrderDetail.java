package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class custOrderDetail extends AppCompatActivity {

    private TextView date,name,email,address,phone,itemtotal,ordertotal,orderconfirm,ship,delivered,orderconfirmmark,shipmark,deliveredmark;
    private ListView productlistdetails;
    private String orderstatus;
    long count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_order_detail);
        getSupportActionBar().hide();

        Toolbar custorderdetailtool = findViewById(R.id.custorderdetailtoolbar);
        String orderid = getIntent().getStringExtra("OrderId");

        SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_CUSTOMER);
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        String _custemail = userDetails.get(SessionManager.KEY_EMAIL);
        String _custpassword = userDetails.get(SessionManager.KEY_PASSWORD);
        String _custname = userDetails.get(SessionManager.KEY_NAME);
        String storetitle = userDetails.get(SessionManager.KEY_SELECTSTORENAME);

        date = findViewById(R.id.custorderdatedetail);
        name = findViewById(R.id.custnameorderdtl);
        email = findViewById(R.id.custemailorderdtls);
        address = findViewById(R.id.custaddorderdtls);
        productlistdetails = findViewById(R.id.custorderprdctlistdetails);
        phone = findViewById(R.id.custphoneorderdtls);
        itemtotal = findViewById(R.id.custitemtotalordertotal);
        ordertotal = findViewById(R.id.custordertotalordertotal);
        orderconfirm = findViewById(R.id.orderconfirmtxt);
        ship = findViewById(R.id.ordershiptxt);
        delivered = findViewById(R.id.orderdelivertxt);
        orderconfirmmark = findViewById(R.id.orderconfirmedmark);
        shipmark = findViewById(R.id.ordershipmark);
        deliveredmark = findViewById(R.id.orderdelivermark);


        custorderdetailtool.setTitle("Order #"+orderid);

        custorderdetailtool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Merchants").child(storetitle).child("orders").child(orderid);
        DatabaseReference dtbr = FirebaseDatabase.getInstance().getReference("Merchants").child(storetitle).child("orders").child(orderid).child("products");

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                date.setText(snapshot.child("Date").getValue(String.class));

                orderstatus = snapshot.child("orderStatus").getValue().toString();
                if(orderstatus.equals("Accepted")){
                    orderconfirm.setText("Order Confirmed");
                    orderconfirmmark.setVisibility(View.VISIBLE);
                }
                if(orderstatus.equals("Shipped")){
                    orderconfirm.setText("Order Confirmed");
                    orderconfirmmark.setVisibility(View.VISIBLE);
                    ship.setText("Order Shipped");
                    shipmark.setVisibility(View.VISIBLE);
                }
                if(orderstatus.equals("Delivered")){
                    orderconfirm.setText("Order Confirmed");
                    orderconfirmmark.setVisibility(View.VISIBLE);
                    ship.setText("Order Shipped");
                    shipmark.setVisibility(View.VISIBLE);
                    delivered.setText("Order Delivered");
                    deliveredmark.setVisibility(View.VISIBLE);
                }
                if(orderstatus.equals("Order Cancelled")){
                    orderconfirm.setText("Order Cancelled");
                    orderconfirm.setTextColor(orderconfirm.getContext().getResources().getColor(R.color.red));

                }

                name.setText(snapshot.child("customerName").getValue(String.class));
                email.setText(snapshot.child("custEmail").getValue(String.class));
                address.setText(snapshot.child("Address").getValue(String.class));
                phone.setText(snapshot.child("phoneno").getValue().toString());
                itemtotal.setText("\u20B9"+snapshot.child("itemTotal").getValue().toString());
                ordertotal.setText("\u20B9"+snapshot.child("ordertotal").getValue().toString());
                count = snapshot.child("products").getChildrenCount();

                setHeight(count);
                //Toast.makeText(getApplicationContext(),String.valueOf(count),Toast.LENGTH_SHORT).show();
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

        orderDetailsAdapter adapter = new orderDetailsAdapter(getApplicationContext(),productqnty,productkey,storetitle);

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