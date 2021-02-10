package com.example.supplycrate1;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

public class createcrate extends AppCompatActivity {

     //String[] fruits = {"Apple", "Appy", "Banana", "Cherry", "Dates", "Grape", "Kiwi", "Mango", "Pear"};
    private AppCompatAutoCompleteTextView autoTextView;
    Resources res = getResources();
    String[] myBooks = res.getStringArray(R.array.my_books);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcrate);




        autoTextView = (AppCompatAutoCompleteTextView) findViewById(R.id.textcrate);
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, fruits);
        autoTextView.setThreshold(1); //will start working from first character
        autoTextView.setAdapter(adapter);*/

    }
}