package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private DatabaseReference prddbref,addcartdb,adcartdb;
    private TextView productNameText, productPriceText,productQuantity, productDesc, productCategory,productDiscount;
    private String _productNameText, _productCategory, _productPriceText, _productQuantity, _productUnit, _productDesc, _productImgUrl,_productDiscount,productcartkey,prdQnty;
    private ImageView productImage;
    private boolean productstock;
    private TextView cratequantitytext;
    private Button addCratebtn,crateincbtn,cratedecbtn;

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
        productCategory = findViewById(R.id.pdtlctgry);
        productDiscount = findViewById(R.id.pdtloffer);
        cratequantitytext = findViewById(R.id.cratequantitybtntext);
        crateincbtn = findViewById(R.id.cartincbtn);
        cratedecbtn = findViewById(R.id.cartdecbtn);


        SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_CUSTOMER);
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();


        String _custemail = userDetails.get(SessionManager.KEY_EMAIL);
        String _custpassword = userDetails.get(SessionManager.KEY_PASSWORD);
        String _custname = userDetails.get(SessionManager.KEY_NAME);
        String _storename = userDetails.get(SessionManager.KEY_SELECTSTORENAME);

        

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate  = new SimpleDateFormat("MMM dd,yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String _currentDate = currentDate.format(calendar.getTime());
        String _currentTime = currentTime.format(calendar.getTime());

        String cartkey = _currentDate +" "+ _currentTime;



        String ProductKey = getIntent().getStringExtra("Productkey");
//      String _storename = getIntent().getStringExtra("StoreName");
        prddbref = FirebaseDatabase.getInstance().getReference("Merchants").child(_storename).child("Products").child(ProductKey);
        addcartdb = FirebaseDatabase.getInstance().getReference("Customers").child(_custname).child("Cart").child(_storename);
        adcartdb = FirebaseDatabase.getInstance().getReference("Customers").child(_custname);

        addcartdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(ProductKey.equals(dataSnapshot.getValue(CartHelper.class).getProductKey())){

                        productcartkey = dataSnapshot.getValue(CartHelper.class).getProductCartKey();
                        prdQnty = dataSnapshot.getValue(CartHelper.class).getProductQuantity();
                        addCratebtn.setVisibility(View.INVISIBLE);
                        cratequantitytext.setVisibility(View.VISIBLE);
                        crateincbtn.setVisibility(View.VISIBLE);
                        cratedecbtn.setVisibility(View.VISIBLE);
                        //Toast.makeText(getApplicationContext(),prdQnty[0],Toast.LENGTH_SHORT).show();
                        setQuantitytext(prdQnty);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        prddbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                _productCategory = snapshot.getValue(ProductHelper.class).getProductCategory();
                _productImgUrl = snapshot.getValue(ProductHelper.class).getProductImageUrl();

                _productQuantity = snapshot.getValue(ProductHelper.class).getProductQuantity();
                _productNameText = snapshot.getValue(ProductHelper.class).getProductName();
                _productPriceText = snapshot.getValue(ProductHelper.class).getSellingprice();
                _productUnit = snapshot.getValue(ProductHelper.class).getProductUnit();
                productstock = snapshot.getValue(ProductHelper.class).isStock();
                if(!productstock){
                    addCratebtn.setText("OUT OF STOCK");
                    addCratebtn.setEnabled(false);
                }

                //cratequantitytext.setText(snapshot.getValue(ProductHelper.class).getProductQuantity());
                productCategory.setText(snapshot.getValue(ProductHelper.class).getProductCategory());
                _productDiscount = snapshot.getValue(ProductHelper.class).getProductDiscount();
                productDiscount.setText(_productDiscount+"% OFF");
                if(_productDiscount.equals("0")){
                    productDiscount.setVisibility(View.INVISIBLE);
                }
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

        crateincbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = Integer.parseInt(cratequantitytext.getText().toString());
                counter++;
                if(counter>100){

                    Toast.makeText(getApplicationContext(),"Quantity cannot be greater than 100",Toast.LENGTH_SHORT).show();
                }
                else{
                    setQuantityonDB(counter,ProductKey,productcartkey);
                    cratequantitytext.setText(String.valueOf(counter));
                }
            }
        });

        cratedecbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = Integer.parseInt(cratequantitytext.getText().toString());
                counter--;
                if(counter<1){
                    addcartdb.child(productcartkey).removeValue();
                    Toast.makeText(getApplicationContext(),"Product is removed from the cart",Toast.LENGTH_SHORT).show();
                    addCratebtn.setVisibility(View.VISIBLE);
                    cratequantitytext.setVisibility(View.INVISIBLE);
                    crateincbtn.setVisibility(View.INVISIBLE);
                    cratedecbtn.setVisibility(View.INVISIBLE);
                }
                else{
                    setQuantityonDB(counter,ProductKey,productcartkey);
                    cratequantitytext.setText(String.valueOf(counter));
                }
            }
        });




        addCratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProjectDetailsPage.this,"Product is added in the crate",Toast.LENGTH_SHORT).show();
                CartHelper cartHelper = new CartHelper(_productNameText,_productUnit,_productPriceText,_productCategory,_productImgUrl,ProductKey,"1",cartkey);
                adcartdb.child("Cart").child(_storename).child(cartkey).setValue(cartHelper);

            }
        });

    }

    private void setQuantitytext(String prdaQnty) {
        //Toast.makeText(getApplicationContext(),"M:"+prdaQnty,Toast.LENGTH_SHORT).show();
        cratequantitytext.setText(prdaQnty);
    }

    private void setQuantityonDB(int Quantity, String ProductKey, String cartkey) {

        addcartdb.child(cartkey).child("productQuantity").setValue(String.valueOf(Quantity));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(getApplicationContext(),CustomerDashboard.class));
    }
}