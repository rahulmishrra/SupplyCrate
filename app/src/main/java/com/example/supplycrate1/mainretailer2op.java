package com.example.supplycrate1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class mainretailer2op extends AppCompatActivity {
    EditText editemail, editpassword;
    Button Login;
    TextView signup, forgotpass;
    FirebaseAuth fAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mainretailer2op);

        editemail = findViewById(R.id.editTextusername1);
        editpassword = findViewById(R.id.editTextpassword1);
        signup = findViewById(R.id.gotosignup);
        forgotpass = findViewById(R.id.textforgotpassword);
        Login = findViewById(R.id.BTN1);



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editemail.getText().toString().trim();
                String password = editpassword.getText().toString().trim();
                fAuth = FirebaseAuth.getInstance();
                if(TextUtils.isEmpty(email))
                {
                    editemail.setError("Email is needed");
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    editpassword.setError("Password is needed");
                    return;
                }

                if(password.length() < 6)
                {
                    editpassword.setError("Password must be more than 6 characters");
                    return;
                }
                else{
                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {

                                Toast.makeText(com.example.supplycrate1.mainretailer2op.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),dashboardopop.class));

                            }
                            else
                            {
                                Toast.makeText(com.example.supplycrate1.mainretailer2op.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }




                        }
                    });
                }








            }



        });




        //signupbutton
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), mainretailer3op.class));
            }
        });
        //resetpass
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter your email, link will be sent here");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //send link
                        String email = resetMail.getText().toString();

                        fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(com.example.supplycrate1.mainretailer2op.this, "Reset link is sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(com.example.supplycrate1.mainretailer2op.this, "Link wasn't sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });



                    }
                });



                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //close

                    }
                });

                passwordResetDialog.create().show();

            }
        });







    }
}