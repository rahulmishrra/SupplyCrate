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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class orderDetailsAdapter extends ArrayAdapter {
    private List<String> ProductQuantity, ProductKey;
    String Bname;
    Context cpcontext;

    public orderDetailsAdapter(@NonNull Context context,  List<String> productQuantity, List<String> productKey, String bname) {
        super(context, R.layout.orderprdctview,productKey);
        this.cpcontext = context;
        this.Bname = bname;
        this.ProductQuantity = productQuantity;
        this.ProductKey = productKey;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(cpcontext).inflate(R.layout.orderprdctview,parent,false);


        TextView prdctname = view.findViewById(R.id.prdnamecarddetails);
        TextView prdctunit = view.findViewById(R.id.unitcarddetails);
        TextView prdctprice = view.findViewById(R.id.prdctpricecarddetails);
        TextView prdctqnty = view.findViewById(R.id.prdctqntycarddetails);
        ImageView prdctimg = view.findViewById(R.id.productimgdetails);

        prdctqnty.setText("Quantity: "+ProductQuantity.get(position));
        String productkey = ProductKey.get(position);
        DatabaseReference data =  FirebaseDatabase.getInstance().getReference("Merchants").child(Bname).child("Products").child(productkey);

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prdctname.setText(snapshot.getValue(ProductHelper.class).getProductName());
                prdctunit.setText(snapshot.getValue(ProductHelper.class).getProductUnit());
                prdctprice.setText("\u20B9"+snapshot.getValue(ProductHelper.class).getSellingprice());
                Picasso.get().load(Uri.parse(snapshot.getValue(ProductHelper.class).getProductImageUrl())).into(prdctimg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }
}

