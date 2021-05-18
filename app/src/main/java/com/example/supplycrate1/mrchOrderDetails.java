package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
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

public class mrchOrderDetails extends AppCompatActivity {

    private TextView date,orderStatus,name,email,address,phone,itemtotal,ordertotal;
    private ListView productlistdetails;
    private Button orderstatusbtn,cancelbtn;
    private String orderstatus;
    private RatingBar ratingBar;
    long count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrch_order_details);
        getSupportActionBar().hide();

        Toolbar mrchorderdetailtool = findViewById(R.id.mrchorderdetailtoolbar);
        String orderid = getIntent().getStringExtra("OrderId");

        SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_MERCHANT);
        HashMap<String,String> mrchDetails = sessionManager.getMerchantDetailFromSession();
        String mbname = mrchDetails.get(SessionManager.KEY_MERCHANTBNAME);

        date = findViewById(R.id.orderdatedetail);
        orderStatus = findViewById(R.id.orderstatusdetails);
        name = findViewById(R.id.custnameorderdetail);
        email = findViewById(R.id.custemailorderdetails);
        address = findViewById(R.id.custaddorderdetails);
        productlistdetails = findViewById(R.id.orderprdctlistdetails);
        phone = findViewById(R.id.custphoneorderdetails);
        itemtotal = findViewById(R.id.itemtotalordertotal);
        ordertotal = findViewById(R.id.ordertotalordertotal);
        orderstatusbtn = findViewById(R.id.orderstatusbtn);
        cancelbtn = findViewById(R.id.cancelorderbutton);
        ratingBar = findViewById(R.id.ratingBar);


        mrchorderdetailtool.setTitle("Order #"+orderid);

        mrchorderdetailtool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Merchants").child(mbname).child("orders").child(orderid);
        DatabaseReference dtbr = FirebaseDatabase.getInstance().getReference("Merchants").child(mbname).child("orders").child(orderid).child("products");

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                date.setText(snapshot.child("Date").getValue(String.class));
                orderStatus.setText(snapshot.child("orderStatus").getValue(String.class));
                orderstatus = snapshot.child("orderStatus").getValue().toString();
                if(orderstatus.equals("Accepted")){
                    orderstatusbtn.setText("SHIP ORDER");
                    orderstatusbtn.setBackgroundColor(orderstatusbtn.getContext().getResources().getColor(R.color.orange));
                }
                if(orderstatus.equals("Shipped")){
                    orderstatusbtn.setText("DELIVER ORDER");
                    orderStatus.setTextColor(orderStatus.getContext().getResources().getColor(R.color.orange));
                    orderstatusbtn.setBackgroundColor(orderstatusbtn.getContext().getResources().getColor(R.color.orderblue));
                }
                if(orderstatus.equals("Delivered")){
                    orderstatusbtn.setClickable(false);
                    orderstatusbtn.setText("ORDER DELIVERED");
                    orderStatus.setTextColor(orderStatus.getContext().getResources().getColor(R.color.orderblue));
                    orderstatusbtn.setBackgroundColor(orderstatusbtn.getContext().getResources().getColor(R.color.orderblue));
                    cancelbtn.setVisibility(View.GONE);
                    OrderReview(mbname,orderid);
                }
                if(orderstatus.equals("Order Cancelled")){
                    orderstatusbtn.setVisibility(View.GONE);
                    orderStatus.setTextColor(orderStatus.getContext().getResources().getColor(R.color.red));
                    cancelbtn.setClickable(false);
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


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbr.child("orderStatus").setValue("Order Cancelled");
                orderstatusbtn.setVisibility(View.INVISIBLE);
                cancelbtn.setClickable(false);
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

        orderstatusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),orderstatus,Toast.LENGTH_SHORT).show();
                if(orderstatus.equals("Pending")){
                    dbr.child("orderStatus").setValue("Accepted");
                    orderstatusbtn.setText("SHIP ORDER");
                    orderstatusbtn.setBackgroundColor(orderstatusbtn.getContext().getResources().getColor(R.color.orange));
                }
                if(orderstatus.equals("Accepted")){
                    dbr.child("orderStatus").setValue("Shipped");
                    orderstatusbtn.setText("DELIVER ORDER");
                    orderstatusbtn.setBackgroundColor(orderstatusbtn.getContext().getResources().getColor(R.color.orderblue));
                }
                if(orderstatus.equals("Shipped")){
                    dbr.child("orderStatus").setValue("Delivered");
                    orderstatusbtn.setClickable(false);
                    orderstatusbtn.setText("ORDER DELIVERED");
                    setSellcount(mbname,productkey);
                    orderstatusbtn.setBackgroundColor(orderstatusbtn.getContext().getResources().getColor(R.color.orderblue));
                    cancelbtn.setVisibility(View.INVISIBLE);
                }

            }
        });





    }

    private void OrderReview(String mbname, String orderid) {
        ratingBar.setVisibility(View.VISIBLE);
        DatabaseReference dbrrr = FirebaseDatabase.getInstance().getReference("Merchants").child(mbname).child("orders").child(orderid);
        dbrrr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("review")){
                    String review  =  snapshot.child("review").getValue().toString();
                    ratingBar.setRating(Float.valueOf(review));
                    ratingBar.setEnabled(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setSellcount(String bname, List<String> productkey) {
        for(int i=0;i<productkey.size();i++){
            DatabaseReference datar = FirebaseDatabase.getInstance().getReference("Merchants").child(bname).child("Products").child(productkey.get(i));
            datar.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild("sellcount")){
                        String sellcount = snapshot.child("sellcount").getValue().toString();
                        int sellcounter = Integer.valueOf(sellcount)+1;
                        datar.child("sellcount").setValue(String.valueOf(sellcounter));
                    }
                    else{
                        datar.child("sellcount").setValue("1");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}