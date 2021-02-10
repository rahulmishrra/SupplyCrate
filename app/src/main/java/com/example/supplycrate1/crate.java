package com.example.supplycrate1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class crate extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String[] language ={"mango","carrot","peach","coconut","cauliflower","papaya","watermelon","spinach","tomato","capsicum","cabbage"};
    TextView show;
    int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crate);
        getSupportActionBar().hide();





        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,language);

        AutoCompleteTextView actv =  (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);
        actv.setAdapter(adapter);
        actv.setTextColor(Color.DKGRAY);
        actv.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        if(i==0){
            show = (TextView) findViewById(R.id.display);
            i=i+1;
        }
        else if(i==1){
            show = (TextView) findViewById(R.id.display1);
            i=i+1;
        }
        else if(i==2){
        show = (TextView) findViewById(R.id.display2);
        i=i+1;
        }
        else if(i==3){
        show = (TextView) findViewById(R.id.display3);
        i=i+1;
        }
        else if(i==4){
        show = (TextView) findViewById(R.id.display4);
        i=i+1;
        }
        else if(i==5){
            show = (TextView) findViewById(R.id.display5);
            i=i+1;
        }
        else {
            show = (TextView) findViewById(R.id.display6);
            i=i+1;
        }


        String item = parent.getItemAtPosition(position).toString();


        Toast.makeText(crate.this, "Item added \t" + item, Toast.LENGTH_LONG).show();


        show.setText(item);

    }


}