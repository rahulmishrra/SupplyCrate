package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProjectDetailsPage extends AppCompatActivity {
    private DatabaseReference prddbref;
    private TextView productNameText, productPriceText,productQuantity, productDesc;
    private String _productNameText, _productPriceText, _productQuantity, _productUnit, _productDesc, _productImgUrl;
    private ImageView productImage;
    private Button addCratebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details_page);
        getSupportActionBar().hide();

        productNameText = findViewById(R.id.pdtlname);
        productPriceText = findViewById(R.id.pdtlprice);
        productDesc = findViewById(R.id.pdtldescription);
        productQuantity = findViewById(R.id.pdtlquantity);
        productImage = findViewById(R.id.pdtlImage);
        addCratebtn = findViewById(R.id.Addtocrate);


        String ProductKey = getIntent().getStringExtra("Productkey");
        String StoreName = getIntent().getStringExtra("StoreName");
        prddbref = FirebaseDatabase.getInstance().getReference("Merchants").child(StoreName).child("Products").child(ProductKey);


        prddbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                _productNameText = snapshot.getValue(ProductHelper.class).getProductName();
                _productPriceText = snapshot.getValue(ProductHelper.class).getSellingprice();
                _productDesc = snapshot.getValue(ProductHelper.class).getProductDetails();
                _productQuantity = snapshot.getValue(ProductHelper.class).getProductQuantity();
                _productUnit = snapshot.getValue(ProductHelper.class).getProductUnit();
                _productImgUrl = snapshot.getValue(ProductHelper.class).getProductImageUrl();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        productNameText.setText(_productNameText);
        productPriceText.setText(_productPriceText);
        productDesc.setText(_productDesc);
       // productQuantity.setText(_productQuantity+ " "+_productUnit );
       // Picasso.get().load(Uri.parse(_productImgUrl)).into(productImage);


    }

}