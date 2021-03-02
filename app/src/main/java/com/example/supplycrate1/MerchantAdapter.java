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

public class MerchantAdapter extends ArrayAdapter {

    List<String> MerchantName;
    Context _custContext;

    public MerchantAdapter(@NonNull Context context, List<String> merchantName) {
        super(context, R.layout.custdash_card,merchantName);

        this.MerchantName = merchantName;
        this._custContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(_custContext).inflate(R.layout.custdash_card,parent,false);
        TextView text_View = view.findViewById(R.id.merchantnameview);

        text_View.setText(MerchantName.get(position));
        return view;
    }
}
