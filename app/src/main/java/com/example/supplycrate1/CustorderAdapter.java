package com.example.supplycrate1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.supplycrate1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CustorderAdapter extends ArrayAdapter {

    List<String> OrderId;
    String StoreTitle;
    Context _context;

    public CustorderAdapter(@NonNull Context context, List<String> orderId, String storetitle) {
        super(context, R.layout.merchorderview,orderId);

        this.OrderId = orderId;
        this._context = context;
        this.StoreTitle = storetitle;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(_context).inflate(R.layout.merchorderview,parent,false);
        TextView custname = view.findViewById(R.id.custname);
        TextView orderprice = view.findViewById(R.id.orderprice);
        TextView orderid = view.findViewById(R.id.merchorderid);
        TextView orderdate = view.findViewById(R.id.orderdate);
        TextView orderstatus = view.findViewById(R.id.orderstatus);

        /*custname.setText(CustomeName.get(position));
        orderprice.setText("\u20B9"+Orderprice.get(position));*/
        orderid.setText("Order #"+OrderId.get(position));/*
        orderdate.setText(OrderDate.get(position));
        orderstatus.setText(OrderStatus.get(position));*/

        DatabaseReference orderdata = FirebaseDatabase.getInstance().getReference("Merchants").child(StoreTitle).child("orders").child(OrderId.get(position));
        orderdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                custname.setText(snapshot.child("customerName").getValue().toString());
                orderprice.setText("\u20B9"+snapshot.child("ordertotal").getValue().toString());
                orderdate.setText(snapshot.child("Date").getValue().toString());
                orderstatus.setText(snapshot.child("orderStatus").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

}
