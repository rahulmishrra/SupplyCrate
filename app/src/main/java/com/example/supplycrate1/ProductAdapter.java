package com.example.supplycrate1;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;


public class ProductAdapter extends ArrayAdapter {

    List<String> ProductList,ProductUnit,ProductKey;
    List<String> ProductPrice,ProductImage,ProductDiscount;
    List<Boolean> ProductStock;
    Context _pcontext;
    ProductHelper productHelper;

    public ProductAdapter(@NonNull Context context, List<String> productList, List<String> productUnit, List<String> productPrice,List<String> productImage, List<Boolean> productStock, List<String> productKey, List<String> productDiscount) {
        super(context, R.layout.prdctview,productList);

        this.ProductList = productList;
        this.ProductUnit = productUnit;
        this.ProductPrice = productPrice;
        this.ProductImage = productImage;
        this._pcontext = context;
        this.ProductStock = productStock;
        this.ProductKey = productKey;
        this.ProductDiscount = productDiscount;
    }

    SessionManager sessionManager = new SessionManager(getContext(),SessionManager.SESSION_MERCHANT);
    HashMap<String,String> mrchDetails = sessionManager.getMerchantDetailFromSession();
    String mbname = mrchDetails.get(SessionManager.KEY_MERCHANTBNAME);

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Merchants").child(mbname).child("Products");


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(_pcontext).inflate(R.layout.prdctview,parent,false);
        TextView prdctlist = view.findViewById(R.id.prdnamecard);
        TextView prdctunit = view.findViewById(R.id.unitcard);
        TextView prdctprice = view.findViewById(R.id.prdctpricecard);
        ImageView prdctimg = view.findViewById(R.id.prdctimgview);
        Switch prdstockprice = view.findViewById(R.id.prdstockswitch);
        TextView prddiscount = view.findViewById(R.id.offerview);
        Picasso.get().load(Uri.parse(ProductImage.get(position))).into(prdctimg);

        prdctlist.setText(ProductList.get(position));
        prdctunit.setText(ProductUnit.get(position));
        prdctprice.setText("\u20B9"+ProductPrice.get(position));
        prdstockprice.setChecked(ProductStock.get(position));
        prddiscount.setText(ProductDiscount.get(position)+"% OFF");
        if(ProductDiscount.get(position).equals("0")){
            prddiscount.setVisibility(View.INVISIBLE);
        }

        boolean prdstk = prdstockprice.isChecked();
        String post = ProductKey.get(position);

        if(prdstk==true){
            prdstockprice.setText("In Stock");
        }else{
            prdstockprice.setText("Out of Stock");
        }

        prdstockprice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    prdstockprice.setText("In Stock");
                    //Toast.makeText(getContext(),post,Toast.LENGTH_SHORT).show();
                    databaseReference.child(post).child("stock").setValue(isChecked);
                }
                else{

                    prdstockprice.setText("Out of Stock");
                    //Toast.makeText(getContext(),post,Toast.LENGTH_SHORT).show();
                    databaseReference.child(post).child("stock").setValue(isChecked);
                }
            }
        });

        return view;
    }
}
