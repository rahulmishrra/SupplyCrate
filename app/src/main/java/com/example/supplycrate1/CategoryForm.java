package com.example.supplycrate1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CategoryForm extends AppCompatActivity {
    EditText ctgryname;
    Button catergstbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_form);

        ctgryname = findViewById(R.id.categoryname);
        catergstbtn = findViewById(R.id.cateformbtn);
    }
}