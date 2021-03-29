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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CustProductAdapter extends ArrayAdapter {
    private List<String> ProductList, ProductUnit, ProductPrice, ProductCategory,ProductImageURL,ProductKey;
    Context cpcontext;
    String CustomerName;

    public CustProductAdapter(@NonNull Context context, List<String> productList, List<String> productUnit, List<String> productPrice, List<String> productCategory, List<String> productImageURL, List<String> productKey, String customerName) {
        super(context, R.layout.cust_prdctview,productList);
        this.cpcontext = context;
        this.ProductList = productList;
        this.ProductUnit = productUnit;
        this.ProductPrice = productPrice;
        this.ProductCategory = productCategory;
        this.ProductImageURL = productImageURL;
        this.ProductKey = productKey;
        this.CustomerName = customerName;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(cpcontext).inflate(R.layout.cust_prdctview,parent,false);


        TextView prdctlist = view.findViewById(R.id.custprdnamecard);
        TextView prdctunit = view.findViewById(R.id.custunitcard);
        TextView prdctprice = view.findViewById(R.id.custprdctpricecard);
        TextView prdctctgry = view.findViewById(R.id.custprdctctgrycard);
        ImageView prdctimg = view.findViewById(R.id.custproductimg);

        String _prdctlist = ProductList.get(position);
        String _prdctunit = ProductUnit.get(position);
        String _prdctprice = ProductPrice.get(position);
        String _prdctctgry = ProductCategory.get(position);
        String _prdctimgurl = ProductImageURL.get(position);
        String _prdctkey = ProductKey.get(position);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate  = new SimpleDateFormat("MMM dd,yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String _currentDate = currentDate.format(calendar.getTime());
        String _currentTime = currentTime.format(calendar.getTime());

        String cartkey = _currentDate +" "+ _currentTime;

        CartHelper cartHelper = new CartHelper(_prdctlist,_prdctunit,_prdctprice,_prdctctgry,_prdctimgurl,_prdctkey,"1");


        prdctlist.setText(ProductList.get(position));
        prdctunit.setText(ProductUnit.get(position));
        prdctprice.setText("\u20B9"+ProductPrice.get(position));
        prdctctgry.setText(ProductCategory.get(position));
        Picasso.get().load(Uri.parse(ProductImageURL.get(position))).into(prdctimg);

        return view;
    }
}

