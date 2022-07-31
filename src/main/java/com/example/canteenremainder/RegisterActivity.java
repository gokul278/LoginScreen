package com.example.canteenremainder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    EditText email,password,confirmpassword,phonenumber,fullname;
    Button btnregister;
    ProgressBar progressBar;


    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.etregisteremail);
        password = findViewById(R.id.etregisterpassword);
        confirmpassword = findViewById(R.id.etregisterconfirmpassword);
        phonenumber = findViewById(R.id.etregisterphoneno);
        progressBar = findViewById(R.id.registerprogressbar);
        fullname = findViewById(R.id.etregistername);

        btnregister = findViewById(R.id.btnregister);



        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String registeremailtxt = email.getText().toString();
                String registerpasswordtxt = password.getText().toString();
                String registerconfirmpasswordtxt = confirmpassword.getText().toString();
                String registerphonenotxt = phonenumber.getText().toString();
                String registernametxt = fullname.getText().toString();

                if(registeremailtxt.isEmpty() || registerpasswordtxt.isEmpty() || registerconfirmpasswordtxt.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Please fill the fields",Toast.LENGTH_SHORT).show();
                }else if(!registerpasswordtxt.equals(registerconfirmpasswordtxt)){
                    Toast.makeText(RegisterActivity.this,"Please Check the Confirm Password",Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.createUserWithEmailAndPassword(registeremailtxt,registerpasswordtxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                progressBar.setVisibility(View.VISIBLE);

                                FirebaseUser ruser = mAuth.getCurrentUser();
                                String userID = ruser.getUid();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://canteenremainder-default-rtdb.firebaseio.com/").child("UserAccounts").child(userID);
                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("Userid",userID);
                                hashMap.put("E-mail",registeremailtxt);
                                hashMap.put("Password",registerpasswordtxt);
                                hashMap.put("Phone Number",registerphonenotxt);
                                hashMap.put("Full Name",registernametxt);
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this,"Account Created Successfully",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                            finish();
                                        }else{
                                            Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });


                            }else{
                                Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}