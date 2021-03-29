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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProjectDetailsPage extends AppCompatActivity {
    private DatabaseReference prddbref,addcartdb;
    TextView productNameText, productPriceText,productQuantity, productDesc;
    private String _productNameText, _productCategory, _productPriceText, _productQuantity, _productUnit, _productDesc, _productImgUrl;
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

        SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_CUSTOMER);
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        String _custemail = userDetails.get(SessionManager.KEY_EMAIL);
        String _custpassword = userDetails.get(SessionManager.KEY_PASSWORD);
        String _custname = userDetails.get(SessionManager.KEY_NAME);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate  = new SimpleDateFormat("MMM dd,yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String _currentDate = currentDate.format(calendar.getTime());
        String _currentTime = currentTime.format(calendar.getTime());

        String cartkey = _currentDate +" "+ _currentTime;



        String ProductKey = getIntent().getStringExtra("Productkey");
        String StoreName = getIntent().getStringExtra("StoreName");
        prddbref = FirebaseDatabase.getInstance().getReference("Merchants").child(StoreName).child("Products").child(ProductKey);
        addcartdb = FirebaseDatabase.getInstance().getReference("Customers").child(_custname).child("Cart");


        prddbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                _productCategory = snapshot.getValue(ProductHelper.class).getProductCategory();
                _productImgUrl = snapshot.getValue(ProductHelper.class).getProductImageUrl();

                _productQuantity = snapshot.getValue(ProductHelper.class).getProductQuantity();
                _productNameText = snapshot.getValue(ProductHelper.class).getProductName();
                _productPriceText = snapshot.getValue(ProductHelper.class).getSellingprice();
                _productUnit = snapshot.getValue(ProductHelper.class).getProductUnit();


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
                //Toast.makeText(ProjectDetailsPage.this,_productNameText+ " "+_productImgUrl,Toast.LENGTH_SHORT).show();
                CartHelper cartHelper = new CartHelper(_productNameText,_productUnit,_productPriceText,_productCategory,_productImgUrl,ProductKey,_productQuantity);
                addcartdb.child(cartkey).setValue(cartHelper);
            }
        });

    }

}