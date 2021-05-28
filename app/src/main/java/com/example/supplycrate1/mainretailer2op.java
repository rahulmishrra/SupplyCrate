package com.example.supplycrate1;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class mainretailer2op extends AppCompatActivity {
    EditText editemail, editpassword,editbname;
    Button Login;
    TextView signup, forgotpass;
    FirebaseAuth fAuth;
    FirebaseDatabase dbrootnode;
    DatabaseReference dbref;
    private ProgressDialog loadingBar;
    private String _b_phone;
    private boolean rd = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mainretailer2op);

        editbname = findViewById(R.id.editbusinessname);
        editemail = findViewById(R.id.editTextusername1);
        editpassword = findViewById(R.id.editTextpassword1);
        signup = findViewById(R.id.gotosignup);
        forgotpass = findViewById(R.id.textforgotpassword);
        Login = findViewById(R.id.BTN1);
        loadingBar = new ProgressDialog(this);




        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingBar.setTitle("Loging In");
                loadingBar.setMessage("Please wait while we are checking your credentials");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                String bname = editbname.getText().toString();
                String email = editemail.getText().toString();
                String password = editpassword.getText().toString();

                fAuth = FirebaseAuth.getInstance();
                dbrootnode = FirebaseDatabase.getInstance();
                dbref = dbrootnode.getReference("Merchants");
                Query checkUser = dbref.orderByChild("bName").equalTo(bname);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String _b_name = snapshot.child(bname).child("bName").getValue().toString();
                            String _b_email = snapshot.child(bname).child("email").getValue().toString();
                            String _name = snapshot.child(bname).child("name").getValue().toString();
                            String _b_pass = snapshot.child(bname).child("pass").getValue().toString();
                            _b_phone = snapshot.child(bname).child("phone").getValue().toString();
                            SessionManager sessionManager = new SessionManager(mainretailer2op.this,SessionManager.SESSION_MERCHANT);
                            sessionManager.createmrchLoginSession(_b_email,_b_pass,bname,_name,_b_phone);
                            rd =false;
                        }
                        else {
                            loadingBar.dismiss();
                            editbname.setError("No such user exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                merchLogin(email,password);

            }

            private void merchLogin(String email, String password) {
                if(TextUtils.isEmpty(email))
                {
                    loadingBar.dismiss();
                    editemail.setError("Email is needed");
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    loadingBar.dismiss();
                    editpassword.setError("Password is needed");
                    return;
                }

                if(password.length() < 6)
                {
                    loadingBar.dismiss();
                    editpassword.setError("Password must be more than 6 characters");
                    return;
                }
                else{

                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                loadingBar.dismiss();

                                Toast.makeText(com.example.supplycrate1.mainretailer2op.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),RetailerDashboard.class));

                            }
                            else
                            {
                                loadingBar.dismiss();
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}