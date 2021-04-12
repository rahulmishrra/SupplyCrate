package com.example.supplycrate1;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class custCartAdapter extends ArrayAdapter {

    private List<String> ProductList, ProductUnit, ProductPrice, ProductCategory, ProductQuantity,ProductimageUrl,Productkey;
    private Context cpcontext;
    String CustomerName;
    long count;

    public custCartAdapter(@NonNull Context context, List<String> productList, List<String> productUnit, List<String> productPrice, List<String> productCategory,List<String> productQuantity, List<String> productimageUrl, List<String> productkey, String customerName) {
        super(context, R.layout.cust_cartview,productList);

        this.cpcontext = context;
        this.ProductList = productList;
        this.ProductUnit = productUnit;
        this.ProductPrice = productPrice;
        this.ProductCategory = productCategory;
        this.CustomerName = customerName;
        this.ProductQuantity = productQuantity;
        this.ProductimageUrl = productimageUrl;
        this.Productkey = productkey;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(cpcontext).inflate(R.layout.cust_cartview,parent,false);

        TextView prdctlist = view.findViewById(R.id.cartprdnamecard);
        TextView prdctunit = view.findViewById(R.id.cartunitcard);
        TextView prdctprice = view.findViewById(R.id.cartprdctprice);
        TextView prdctctgry = view.findViewById(R.id.cartprdctctgry);
        TextView prdctqnty = view.findViewById(R.id.cartprdctqnty);
        ImageView cartprdimgview = view.findViewById(R.id.cartprdctimgview);


        prdctlist.setText(ProductList.get(position));
        prdctunit.setText(ProductUnit.get(position));
        prdctprice.setText("\u20B9"+ProductPrice.get(position));
        prdctctgry.setText(ProductCategory.get(position));
        prdctqnty.setText("Quantity: "+ProductQuantity.get(position));
        //cartquantitytext.setText(ProductQuantity.get(position));
        Picasso.get().load(Uri.parse(ProductimageUrl.get(position))).into(cartprdimgview);


        String cartentry = String.valueOf(position+1);

        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Customers").child(CustomerName).child("Cart").child(cartentry).child("productQuantity");

        return view;
    }

    private void setQuantity(String quantity,DatabaseReference databaseReference) {
        Toast.makeText(getContext(),quantity,Toast.LENGTH_SHORT).show();
        databaseReference.setValue(quantity);
    }
}
