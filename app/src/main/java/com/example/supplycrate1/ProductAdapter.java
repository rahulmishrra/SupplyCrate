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


public class ProductAdapter extends ArrayAdapter {

    List<String> ProductList,ProductUnit;
    List<String> ProductPrice;
    Context _pcontext;

    public ProductAdapter(@NonNull Context context, List<String> productList, List<String> productUnit, List<String> productPrice) {
        super(context, R.layout.prdctview,productList);

        this.ProductList = productList;
        this.ProductUnit = productUnit;
        this.ProductPrice = productPrice;
        this._pcontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(_pcontext).inflate(R.layout.prdctview,parent,false);
        TextView prdctlist = view.findViewById(R.id.prdnamecard);
        TextView prdctunit = view.findViewById(R.id.unitcard);
        TextView prdctprice = view.findViewById(R.id.prdctpricecard);

        prdctlist.setText(ProductList.get(position));
        prdctunit.setText(ProductUnit.get(position));
        prdctprice.setText("\u20B9"+ProductPrice.get(position));

        return view;
    }
}
