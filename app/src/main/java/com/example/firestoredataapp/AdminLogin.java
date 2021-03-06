package com.example.firestoredataapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLogin extends AppCompatActivity {

    EditText Email,Password;
    Button Login;;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);

        Login =findViewById(R.id.Login);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){

                    Email.setError("Email is required");
                    return;

                }
                if(TextUtils.isEmpty(password)){

                    Password.setError("Password is required");
                    return;

                }
                if(password.length()<6){

                    Password.setError("Password must contain six characters");
                    return;

                }

                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AdminLogin.this,"Sign In Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),SelectOption.class));

                        }else {
                            Toast.makeText(AdminLogin.this,"Error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });
            }
        });
    }
}