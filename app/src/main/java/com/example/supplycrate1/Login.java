package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {


    Animation top,fade;
    ImageView topdesigngreen,logintext;
    private Button Signin;
    private EditText custemail, pass;
    private FirebaseAuth firebaseAuth;
    private TextView Signup;
    private ProgressDialog loadingbar;
    private String locality,location;
    private boolean dataAvailable= false;
    private String name;
    private SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_login);

        Signin = findViewById(R.id.signinbutton);
        custemail = findViewById(R.id.txtemail);
        pass = findViewById(R.id.txtpassword);
        Signup = findViewById(R.id.textView5);
        loadingbar = new ProgressDialog(this);

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingbar.setTitle("Customer Login");
                loadingbar.setMessage("Please wait while we are checking your credentials");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

                String custEmail = custemail.getText().toString();
                String password = pass.getText().toString();
                String custName;



                DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Customers");


                dr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            String emaili = dataSnapshot.getValue(CustHelperClass.class).getEmail();

                            if(emaili.equals(custEmail)){
                                name = dataSnapshot.getValue(CustHelperClass.class).getUsername();
                                /*
                                if(dataSnapshot.hasChild("Location")){
                                    locality = dataSnapshot.child("Locality").getValue().toString();
                                    location = dataSnapshot.child("Location").getValue().toString();
                                    dataAvailable = true;
                                }*/
                                break;
                               // Toast.makeText(getApplicationContext(),"Yeh chal gya" + cname,Toast.LENGTH_SHORT).show();
                            }
                        }
                        sessionManager = new SessionManager(Login.this,SessionManager.SESSION_CUSTOMER);
                        sessionManager.createLoginSession(custEmail,password,name);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadingbar.dismiss();

                    }
                });
                authorizeUser(custEmail,password,name);

            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                v.getContext().startActivity(intent);
            }
        });

        topdesigngreen=findViewById(R.id.imageView8);
        logintext=findViewById(R.id.imageView5);
        top = AnimationUtils.loadAnimation(this,R.anim.topdown);
        fade = AnimationUtils.loadAnimation(this,R.anim.bganim);

        topdesigngreen.setAnimation(top);
        logintext.setAnimation(fade);
    }

    private void authorizeUser(String custEmail, String password, String custName) {
        firebaseAuth = FirebaseAuth.getInstance();
        if(TextUtils.isEmpty(custEmail) || TextUtils.isEmpty(password) ) {
            Toast.makeText(Login.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            loadingbar.dismiss();
        } else{

            firebaseAuth.signInWithEmailAndPassword(custEmail, password)
                    .addOnCompleteListener(Login.this, task -> {
                        if (task.isSuccessful()) {
                            Intent loginIntent = new Intent(getApplicationContext(), CustomerDashboard.class);
                            if(dataAvailable){
                                sessionManager.setLocation(location,locality);
                            }
                            loadingbar.dismiss();
                            loginIntent.putExtra("Login","Login");
                            startActivity(loginIntent);
                            finish();


                        } else {
                            Toast.makeText(Login.this, "Login Failed or User not available",Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}