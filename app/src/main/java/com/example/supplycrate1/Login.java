package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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


public class Login extends AppCompatActivity {


    Animation top,fade;
    ImageView topdesigngreen,logintext;
    Button Signin;
    EditText custemail, pass;
    FirebaseAuth firebaseAuth;
    TextView Signup;



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

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String custEmail = custemail.getText().toString();
                String password = pass.getText().toString();
                firebaseAuth = FirebaseAuth.getInstance();
                if(custEmail.equals("")||password.equals(""))
                    Toast.makeText(Login.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{

                    firebaseAuth.signInWithEmailAndPassword(custEmail, password)
                            .addOnCompleteListener(Login.this, task -> {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getApplicationContext(), CustomerDashboard.class));
                                    SessionManager sessionManager = new SessionManager(Login.this,SessionManager.SESSION_CUSTOMER);
                                    sessionManager.createLoginSession(custEmail,password);


                                } else {
                                    Toast.makeText(Login.this, "Login Failed or User not available",Toast.LENGTH_SHORT).show();

                                }

                            });

                }

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
}