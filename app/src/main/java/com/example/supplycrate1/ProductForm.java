package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ProductForm extends AppCompatActivity{
    EditText prdctname,prdctfile,mrp,sellingprice,quantity;
    ImageView productImage;
    Button productrgstbtn;
    Spinner unit,category;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataref,dbr,dbrefer;
    private StorageReference ProductImageRef;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private boolean stock;
    private String _prdctname, _prdctfile, _mrp, _sellingprice, _quantity, _unit,_category,productkey,productImageUrl,mbname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);
        getSupportActionBar().hide();

        SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_MERCHANT);
        HashMap<String,String> mrchDetails = sessionManager.getMerchantDetailFromSession();
         mbname = mrchDetails.get(SessionManager.KEY_MERCHANTBNAME);


        prdctname = findViewById(R.id.productname);
        prdctfile = findViewById(R.id.productdetails);
        mrp = findViewById(R.id.mrp);
        sellingprice = findViewById(R.id.sellingprice);
        quantity = findViewById(R.id.quantity);
        productrgstbtn = findViewById(R.id.prdformbtn);
        unit = findViewById(R.id.unitspinner);
        category = findViewById(R.id.categoryspinner);
        productImage = findViewById(R.id.productimg);

        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Products");

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(ProductForm.this,R.array.units, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(arrayAdapter);


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
                ValidateProductData();
            }
        });


    }


    private void ValidateProductData() {
        _prdctname = prdctname.getText().toString();
        _prdctfile = prdctfile.getText().toString();
        _mrp = mrp.getText().toString();
        _sellingprice = sellingprice.getText().toString();
        _quantity = quantity.getText().toString();
        _unit = unit.getSelectedItem().toString();
        _category = category.getSelectedItem().toString();

        if(ImageUri == null){
            Toast.makeText(this,"Product Image is mandatory...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(_prdctfile)){
            prdctfile.setError("Product Details is required");
        }
        else if(TextUtils.isEmpty(_prdctname)){
            prdctname.setError("Product Name is required");
        }
        else if(TextUtils.isEmpty(_mrp)){
            mrp.setError("Mrp is required");
        }
        else if(TextUtils.isEmpty(_sellingprice)){
            sellingprice.setError("Selling price is required");
        }
        else if(TextUtils.isEmpty(_quantity)){
            quantity.setError("Quantity is required");
        }
        else {
            StoreProductInformation();
        }


    }

    private void StoreProductInformation() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate  = new SimpleDateFormat("MMM dd,yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String _currentDate = currentDate.format(calendar.getTime());
        String _currentTime = currentTime.format(calendar.getTime());

        productkey = _currentDate +" "+ _currentTime;


        StorageReference filepath = ProductImageRef.child(ImageUri.getLastPathSegment() + productkey + ".jpg");
        final UploadTask uploadTask = filepath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductForm.this,"Error: "+ e.toString(),Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ProductForm.this,"Image uploaded successfully... ",Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

                        productImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            productImageUrl = task.getResult().toString();

                            Toast.makeText(ProductForm.this,"Got Product Image Url Successfully...",Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }
    private void SaveProductInfoToDatabase() {
        stock = true;
        ProductHelper productHelper = new ProductHelper(_prdctname,_prdctfile,_quantity,_unit,_category,_mrp,_sellingprice,productImageUrl,productkey,stock);

        dbr = firebaseDatabase.getReference("Merchants").child(mbname).child("Products");

        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbr.child(productkey).setValue(productHelper);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data != null){
            ImageUri = data.getData();
            productImage.setImageURI(ImageUri);
        }
    }
}