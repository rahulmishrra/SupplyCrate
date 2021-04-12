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

public class QueueAdapter extends ArrayAdapter {

    List<String> CustomerDetail;
    Context _context;

    public QueueAdapter(@NonNull Context context, List<String> customerDetail) {
        super(context, R.layout.queueview, customerDetail);

        this.CustomerDetail = customerDetail;
        this._context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(_context).inflate(R.layout.queueview,parent,false);

        TextView queuecust = view.findViewById(R.id.queuecust);

        queuecust.setText(CustomerDetail.get(position));


        return view;
    }
}
