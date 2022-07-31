package com.example.canteenremainder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText email,password;
    Button btnregister,btnlogin;
    TextView forgetpassword;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        EditText email = findViewById(R.id.etloginmail);
        EditText password = findViewById(R.id.etloginpassword);


        Button btnlogin = findViewById(R.id.btnlogin);
        Button btnregister = findViewById(R.id.singupgotobtn);
        TextView forgetpassword = findViewById(R.id.etforgetpassword);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginemailtxt = email.getText().toString();
                String loginpasswordtxt = password.getText().toString();

                if(TextUtils.isEmpty(loginemailtxt)){
                    email.setError("Email cannot be empty");
                    password.requestFocus();
                }else if(TextUtils.isEmpty(loginpasswordtxt)){
                    password.setError("Password Cannot be empty");
                    password.requestFocus();
                }else{
                    mAuth.signInWithEmailAndPassword(loginemailtxt,loginpasswordtxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser ruser = mAuth.getCurrentUser();
                                String userid = ruser.getUid();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://canteenremainder-default-rtdb.firebaseio.com/").child("UserAccounts").child(userid);
                                HashMap hashMap = new HashMap();
                                hashMap.put("Password",loginpasswordtxt);


                                databaseReference.updateChildren(hashMap);

                                Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(MainActivity.this, "Registeration Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ForgetpasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}