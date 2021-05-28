package com.example.supplycrate1;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class mainretailer3op extends AppCompatActivity {
    ImageView merchImage;
    EditText editName, bname, editEmail, editPassword, editPhone ;
    Button Register;
    TextView Goback;
    private String businessName,name,email,phone,password,storeImageUrl;
    private FirebaseAuth fAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference dbref,dbr;
    private StorageReference StoreImageRef;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private ProgressDialog loadingBar;


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
        merchImage = findViewById(R.id.merchimg);
        loadingBar = new ProgressDialog(this);

        StoreImageRef = FirebaseStorage.getInstance().getReference().child("Store");

        merchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        fAuth = FirebaseAuth.getInstance();

       /* if (fAuth.getCurrentUser() == null)
        {
            startActivity(new Intent(getApplicationContext(), dashboardopop.class));
            finish();
        }*/

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateMerchantdata();

                businessName = bname.getText().toString();
                email = editEmail.getText().toString().trim();
                password = editPassword.getText().toString().trim();
                name = editName.getText().toString().trim();
                phone= editPhone.getText().toString();



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

                }

            }









        });


        Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), mainretailer2op.class));
            }
        });


    }

    private void validateMerchantdata() {
        businessName = bname.getText().toString();
        email = editEmail.getText().toString().trim();
        password = editPassword.getText().toString().trim();
        name = editName.getText().toString().trim();
        phone= editPhone.getText().toString();

        if(ImageUri == null){
            Toast.makeText(getApplicationContext(),"Merchant Image Mandatory",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
        else if(TextUtils.isEmpty(businessName)){
            bname.setError("Please Enter your business Name");
            loadingBar.dismiss();
        }
        else if(TextUtils.isEmpty(email)){
            editEmail.setError("Please enter the email address");
            loadingBar.dismiss();
        }
        else if(TextUtils.isEmpty(password)){
            editPassword.setError("Please enter the email address");
            loadingBar.dismiss();
        }
        else if(TextUtils.isEmpty(name)){
            editEmail.setError("Please enter the email address");
            loadingBar.dismiss();
        }
        else if(TextUtils.isEmpty(businessName)){
            editName.setError("Please enter the email address");
            loadingBar.dismiss();
        }
        else{
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

        loadingBar.setTitle("Add New Business");
        loadingBar.setMessage("Please wait while we are registering your new business");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        StorageReference filepath = StoreImageRef.child(bname + ".jpg");
        final UploadTask uploadTask = filepath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+ e.toString(),Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"Image uploaded successfully... ",Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

                        storeImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            storeImageUrl = task.getResult().toString();

                            Toast.makeText(getApplicationContext(),"Got Product Image Url Successfully...",Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void SaveProductInfoToDatabase() {

        BusinessHelperClass Bhclass = new BusinessHelperClass(businessName,name,email,phone,password,storeImageUrl);
        rootnode = FirebaseDatabase.getInstance();
        dbref = rootnode.getReference("Merchants");
        fAuth = FirebaseAuth.getInstance();

        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    loadingBar.dismiss();
                    dbref.child(businessName).setValue(Bhclass);
                    dbref.child(businessName).child("Location").setValue("");
                    dbref.child(businessName).child("Latitude").setValue("");
                    dbref.child(businessName).child("Longitude").setValue("");
                    dbref.child(businessName).child("Locality").setValue("");
                    dbref.child(businessName).child("PostalCode").setValue("");
                    //dbr.child(email).setValue(Bhclass);
                    SessionManager sessionManager = new SessionManager(mainretailer3op.this,SessionManager.SESSION_MERCHANT);
                    sessionManager.createmrchLoginSession(email,password,businessName,name,phone);
                    Toast.makeText(com.example.supplycrate1.mainretailer3op.this, "User Created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MerchLocation.class));
                }
                else
                {
                    Toast.makeText(com.example.supplycrate1.mainretailer3op.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data != null){
            ImageUri = data.getData();
            merchImage.setImageURI(ImageUri);
        }
    }

}
