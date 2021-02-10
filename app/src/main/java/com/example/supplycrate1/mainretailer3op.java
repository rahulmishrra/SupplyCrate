package com.example.supplycrate1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class mainretailer3op extends AppCompatActivity {
    EditText editName, bname, editEmail, editPassword, editPhone;
    Button Register;
    TextView Goback;
    private FirebaseAuth fAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference dbref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mainretailer3op);

        bname = findViewById(R.id.bname);
        editName = findViewById(R.id.editTextusername);
        editEmail = findViewById(R.id.editTextemailaddress);
        editPassword = findViewById(R.id.editTextpassword2);
        editPhone = findViewById(R.id.editTextPhone);
        Register = findViewById(R.id.BTN1);
        Goback = findViewById(R.id.textsignin);

        fAuth = FirebaseAuth.getInstance();

       /* if (fAuth.getCurrentUser() == null)
        {
            startActivity(new Intent(getApplicationContext(), dashboardopop.class));
            finish();
        }*/

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String businessName = bname.getText().toString();
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                String name = editName.getText().toString().trim();
                String phone= editPhone.getText().toString();
                BusinessHelperClass Bhclass = new BusinessHelperClass(businessName,name,email,phone,password);
                rootnode = FirebaseDatabase.getInstance();
                dbref = rootnode.getReference("Merchants");
                fAuth = FirebaseAuth.getInstance();


                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(businessName) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone))
                {
                    editEmail.setError("Please enter all input fields");
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    editPassword.setError("Password is needed");
                    return;
                }

                if(password.length() < 6)
                {
                    editPassword.setError("Password must be more than 6 characters");
                    return;
                }
                else{
                    //registering user FIREBASE
                    fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                dbref.child(businessName).setValue(Bhclass);
                                Toast.makeText(com.example.supplycrate1.mainretailer3op.this, "User Created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),dashboardopop.class));
                            }
                            else
                            {
                                Toast.makeText(com.example.supplycrate1.mainretailer3op.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }






            }









        });


        Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), mainretailer2op.class));
            }
        });


    };


}
