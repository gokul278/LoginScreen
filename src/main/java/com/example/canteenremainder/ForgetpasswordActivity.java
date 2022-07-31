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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgetpasswordActivity extends AppCompatActivity {


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ProgressBar resetprogressbar;
    EditText forgetemail;
    TextView resetstate;
    Button resetbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgetpassword);

        forgetemail = findViewById(R.id.forgetemail);
        resetprogressbar = findViewById(R.id.resetrprogressbar);
        resetbtn = findViewById(R.id.resetbtn);
        resetstate = findViewById(R.id.resetstate);

        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(forgetemail.getText().toString().isEmpty()){
                    resetstate.setText("Please Enter the E-MAIL");
                }else{
                    resetprogressbar.setVisibility(View.VISIBLE);
                    mAuth.fetchSignInMethodsForEmail(forgetemail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if(task.getResult().getSignInMethods().isEmpty()){
                                resetprogressbar.setVisibility(View.GONE);
                                resetstate.setText("This is not an registered email,you can create new Account");
                            }else{
                                mAuth.sendPasswordResetEmail(forgetemail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task){
                                        resetprogressbar.setVisibility(View.GONE);
                                        if(task.isSuccessful()){
                                            resetstate.setText("An reset email has been sent to your email address");
                                            Toast.makeText(ForgetpasswordActivity.this, "An reset email has been sent to your email address", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ForgetpasswordActivity.this,MainActivity.class);
                                            startActivity(intent);
                                        }
                                        else{
                                            resetstate.setText(task.getException().getMessage());
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}