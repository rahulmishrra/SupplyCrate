package com.example.supplycrate1;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MerchantAdapter extends ArrayAdapter {

    List<String> MerchantName,MerchantImageURi;
    Context _custContext;

    public MerchantAdapter(@NonNull Context context, List<String> merchantName, List<String> merchantImageURi) {
        super(context, R.layout.custdash_card,merchantName);

        this.MerchantName = merchantName;
        this._custContext = context;
        this.MerchantImageURi = merchantImageURi;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(_custContext).inflate(R.layout.custdash_card,parent,false);
        TextView text_View = view.findViewById(R.id.merchantnameview);
        ImageView merchImg = view.findViewById(R.id.merchcustImage);

        Picasso.get().load(Uri.parse(MerchantImageURi.get(position))).into(merchImg);
        text_View.setText(MerchantName.get(position));
        return view;
    }
}
