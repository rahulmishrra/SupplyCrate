package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductForm extends AppCompatActivity{
    EditText prdctname,prdctfile,mrp,sellingprice,quantity;
    Button productrgstbtn;
    Spinner unit,category;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataref,dbr,dbrefer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);
        getSupportActionBar().hide();


        prdctname = findViewById(R.id.productname);
        prdctfile = findViewById(R.id.productdetails);
        mrp = findViewById(R.id.mrp);
        sellingprice = findViewById(R.id.sellingprice);
        quantity = findViewById(R.id.quantity);
        productrgstbtn = findViewById(R.id.prdformbtn);
        unit = findViewById(R.id.unitspinner);
        category = findViewById(R.id.categoryspinner);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(ProductForm.this,R.array.units, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(arrayAdapter);

        SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_MERCHANT);
        HashMap<String,String> mrchDetails = sessionManager.getMerchantDetailFromSession();
        String mbname = mrchDetails.get(SessionManager.KEY_MERCHANTBNAME);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataref = firebaseDatabase.getReference("Merchants").child(mbname).child("Categories");

        List<String> categories = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                categories.add(snapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                categories.remove(snapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        category.setAdapter(adapter);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                //Toast.makeText(ProductForm.this,item,Toast.LENGTH_SHORT).show();
                category.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



/*
        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<String> categories = new ArrayList<String>();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    categories.add(dataSnapshot.getValue().toString());

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProductForm.this, android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                category.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        productrgstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _prdctname = prdctname.getText().toString();
                String _prdctfile = prdctfile.getText().toString();
                String _mrp = mrp.getText().toString();
                String _sellingprice = sellingprice.getText().toString();
                String _quantity = quantity.getText().toString();
                String _unit = unit.getSelectedItem().toString();
                String _category = category.getSelectedItem().toString();
                boolean stock = true;
                ProductHelper productHelper = new ProductHelper(_prdctname,_prdctfile,_quantity,_unit,_category,_mrp,_sellingprice,stock);

                dbr = firebaseDatabase.getReference("Merchants").child(mbname);


                dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("Products")){
                            newProducts(_prdctname,_prdctfile,_quantity,_unit,_category,_mrp,_sellingprice,stock);
                        }
                        else{
                            dbr.child("Products").child("1").setValue(productHelper);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




                Toast.makeText(ProductForm.this,_prdctname+" "+ _prdctfile + " " + _mrp + " " + _sellingprice + " " + _quantity + " " + _unit + " " +_category,Toast.LENGTH_SHORT).show();
            }

            private void newProducts(String productName, String productDetails, String productQuantity, String productUnit, String productCategory, String mrp, String sellingprice, boolean Stock) {

                ProductHelper producthelper = new ProductHelper(productName,productDetails,productQuantity,productUnit,productCategory,mrp,sellingprice,Stock);
                dbrefer = firebaseDatabase.getReference("Merchants").child(mbname).child("Products");
                dbrefer.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long count = snapshot.getChildrenCount();
                        String num = String.valueOf(count+1);
                        dbrefer.child(num).setValue(producthelper);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        });


    }
}