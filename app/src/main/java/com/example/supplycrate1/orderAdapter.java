package com.example.supplycrate1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class orderAdapter extends ArrayAdapter {

    List<String> CustomeName,Orderprice,OrderId,OrderDate,OrderStatus;
    Context _context;

    public orderAdapter(@NonNull Context context, List<String> customeName, List<String> orderprice, List<String> orderId, List<String> orderDate, List<String> orderStatus) {
        super(context, R.layout.merchorderview,customeName);

        this.CustomeName = customeName;
        this.Orderprice = orderprice;
        this.OrderId = orderId;
        this.OrderDate = orderDate;
        this.OrderStatus = orderStatus;
        this._context = context;

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

        custname.setText(CustomeName.get(position));
        orderprice.setText("\u20B9"+Orderprice.get(position));
        orderid.setText("Order #"+OrderId.get(position));
        orderdate.setText(OrderDate.get(position));
        orderstatus.setText(OrderStatus.get(position));
        return view;
    }

}
