package com.example.supplycrate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    Animation top,fade;
    ImageView topdesigngreen, signuptext;
    private Button Signup;
    private EditText name,email,pass,phone;

    private TextView login;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase rootNode;
    private DatabaseReference dbref;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_sign_up);


        Signup = (Button) findViewById(R.id.signupbtn);
        name = (EditText)findViewById(R.id.txtusername);
        email = (EditText)findViewById(R.id.txtemail);
        pass = (EditText)findViewById(R.id.txtpassword);
        phone = (EditText)findViewById(R.id.txtphone);

        login = (TextView) findViewById(R.id.textView5);
        loadingbar = new ProgressDialog(this);


       Signup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               loadingbar.setTitle("Customer Registration");
               loadingbar.setMessage("Please wait while we are registering your account");
               loadingbar.setCanceledOnTouchOutside(false);
               loadingbar.show();

               String username = name.getText().toString();
               String mail = email.getText().toString();
               String password = pass.getText().toString();
               String phoneno = phone.getText().toString();
               CustHelperClass custHelper = new CustHelperClass(username, mail, password, phoneno);
               rootNode = FirebaseDatabase.getInstance();
               dbref = rootNode.getReference("Customers");
               firebaseAuth = FirebaseAuth.getInstance();

               if (username.equals("") || mail.equals("") || password.equals("") || phoneno.equals("")){
                   Toast.makeText(SignUp.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
               }
               else{


                   firebaseAuth.createUserWithEmailAndPassword(mail, password)
                           .addOnCompleteListener(SignUp.this, task -> {


                               if (task.isSuccessful()) {
                                   SessionManager sessionManager = new SessionManager(SignUp.this,SessionManager.SESSION_CUSTOMER);
                                   sessionManager.createLoginSession(mail,password,username);
                                   dbref.child(username).setValue(custHelper);
                                   dbref.child(username).child("Cart").setValue("abc");
                                   dbref.child(username).child("Latitude").setValue(0.0);
                                   dbref.child(username).child("Locality").setValue("abc");
                                   dbref.child(username).child("Location").setValue("abc");
                                   dbref.child(username).child("Longitude").setValue(0.0);
                                   dbref.child(username).child("PostalCode").setValue("abc");

                                   loadingbar.dismiss();
                                   Toast.makeText(SignUp.this,"Registration Complete", Toast.LENGTH_SHORT).show();
                                   startActivity(new Intent(getApplicationContext(), CustomerDashboard.class));
                               } else {
                                   Toast.makeText(SignUp.this,"Authentication Failed", Toast.LENGTH_SHORT).show();
                                   loadingbar.dismiss();
                               }
                           });
               }
           }
       });

       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), Login.class);
               startActivity(intent);
           }
       });


        topdesigngreen=findViewById(R.id.topdesign);
        signuptext=findViewById(R.id.zoomlogo);
        top = AnimationUtils.loadAnimation(this,R.anim.topdown);
        fade = AnimationUtils.loadAnimation(this,R.anim.bganim);


        topdesigngreen.setAnimation(top);
        signuptext.setAnimation(fade);

    }
}