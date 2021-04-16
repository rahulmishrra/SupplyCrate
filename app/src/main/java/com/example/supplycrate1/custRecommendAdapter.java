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

import java.util.List;

public class custRecommendAdapter extends ArrayAdapter {

    List<String> ProductKey;
    String StoreName;
    Context _context;

    public custRecommendAdapter(@NonNull Context context, List<String> productKey, String storeName) {
        super(context,R.layout.recommendview,productKey);
        this._context = context;
        this.ProductKey = productKey;
        this.StoreName = storeName;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(_context).inflate(R.layout.recommendview,parent,false);

        TextView prdctname = view.findViewById(R.id.recomprdnamecard);
        TextView prdctunit = view.findViewById(R.id.recomunitcard);
        TextView prdctgry = view.findViewById(R.id.recomprdctctgry);
        TextView prdctprice = view.findViewById(R.id.recomprdctprice);
        TextView prdctoffer= view.findViewById(R.id.recomprdct);
        ImageView prdctimg = view.findViewById(R.id.recomprdctimgview);

        String productkey  = ProductKey.get(position);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Merchants").child(StoreName).child("Products").child(productkey);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prdctname.setText(snapshot.getValue(ProductHelper.class).getProductName());
                prdctunit.setText(snapshot.getValue(ProductHelper.class).getProductQuantity()+snapshot.getValue(ProductHelper.class).getProductUnit());
                prdctgry.setText(snapshot.getValue(ProductHelper.class).getProductCategory());
                prdctprice.setText("\u20B9"+ snapshot.getValue(ProductHelper.class).getSellingprice());
                prdctoffer.setText(snapshot.getValue(ProductHelper.class).getProductDiscount()+"% OFF");
                if(snapshot.getValue(ProductHelper.class).getProductDiscount().equals("0")){
                    prdctoffer.setVisibility(View.INVISIBLE);
                }
                Picasso.get().load(Uri.parse(snapshot.getValue(ProductHelper.class).getProductImageUrl())).into(prdctimg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
