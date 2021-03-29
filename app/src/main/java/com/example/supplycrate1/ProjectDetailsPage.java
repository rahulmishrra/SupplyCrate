package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProjectDetailsPage extends AppCompatActivity {
    private DatabaseReference prddbref;
    TextView productNameText, productPriceText,productQuantity, productDesc;
    private String _productNameText, _productPriceText, _productQuantity, _productUnit, _productDesc, _productImgUrl;
    private ImageView productImage;
    private Button addCratebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details_page);
        getSupportActionBar().hide();

        productNameText = (TextView) findViewById(R.id.pdtlname);
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

                productNameText.setText(snapshot.getValue(ProductHelper.class).getProductName());
                productPriceText.setText("\u20B9"+snapshot.getValue(ProductHelper.class).getSellingprice());
                productDesc.setText(snapshot.getValue(ProductHelper.class).getProductDetails());
                productQuantity.setText(snapshot.getValue(ProductHelper.class).getProductQuantity() + " " + snapshot.getValue(ProductHelper.class).getProductUnit());
                Picasso.get().load(Uri.parse(snapshot.getValue(ProductHelper.class).getProductImageUrl())).into(productImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        addCratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProjectDetailsPage.this,_productNameText+ " "+_productImgUrl,Toast.LENGTH_SHORT).show();
            }
        });

    }

}