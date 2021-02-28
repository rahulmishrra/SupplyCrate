package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CategoryForm extends AppCompatActivity {
    EditText ctgryname;
    Button catergstbtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbref,databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_form);

        ctgryname = findViewById(R.id.categoryname);
        catergstbtn = findViewById(R.id.cateformbtn);

        SessionManager sessionManager = new SessionManager(CategoryForm.this,SessionManager.SESSION_MERCHANT);
        HashMap<String,String> merchDetails = sessionManager.getMerchantDetailFromSession();

        String mrmail  = merchDetails.get(SessionManager.KEY_MERCHANTEMAIL);
        String mrpass  = merchDetails.get(SessionManager.KEY_MERCHANTPASSWORD);

        catergstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryname = ctgryname.getText().toString();
                firebaseDatabase = FirebaseDatabase.getInstance();
                dbref = firebaseDatabase.getReference("Pradeep");
                databaseReference = firebaseDatabase.getReference("Pradeep").child("Categories");


                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("Categories")){
                            newCategory(categoryname);

                        }
                        else{
                            dbref.child("Categories").child("1").setValue(categoryname);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }

            private void newCategory(String categorynam) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long c = snapshot.getChildrenCount();
                        String num = String.valueOf(c+1);
                        //Toast.makeText(CategoryForm.this,"alksdjflksjd" + num,Toast.LENGTH_SHORT).show();
                        databaseReference.child(num).setValue(categorynam);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}