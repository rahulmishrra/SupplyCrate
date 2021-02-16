package com.example.supplycrate1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ProductForm extends AppCompatActivity {
    EditText prdctname,prdctfile,mrp,sellingprice,quantity;
    Button productrgstbtn;
    Spinner unit,category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);


        prdctname = findViewById(R.id.productname);
        prdctfile = findViewById(R.id.productdetails);
        mrp = findViewById(R.id.mrp);
        sellingprice = findViewById(R.id.sellingprice);
        quantity = findViewById(R.id.quantity);
        productrgstbtn = findViewById(R.id.prdformbtn);
        unit = findViewById(R.id.unitspinner);
        category = findViewById(R.id.categoryspinner);
    }
}