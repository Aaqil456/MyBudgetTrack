package com.example.mybudgettrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class register_user extends AppCompatActivity {
    Button btnDaftar;
    EditText etEmail, etPassword;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        etEmail=findViewById(R.id.email);
        etPassword=findViewById(R.id.password);
        btnDaftar=findViewById(R.id.daftar_button);
        mAuth=FirebaseAuth.getInstance();

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password= etPassword.getText().toString().trim();
                if(email.isEmpty())
                {
                    etEmail.setError("Email is empty");
                    etEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    etEmail.setError("Enter the valid email address");
                    etEmail.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    etPassword.setError("Enter the password");
                    etPassword.requestFocus();
                    return;
                }
                if(password.length()<6)
                {
                    etPassword.setError("Length of the password should be more than 6");
                    etPassword.requestFocus();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    Intent intent;

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(register_user.this,"You are successfully Registered", Toast.LENGTH_SHORT).show();
                            intent= new Intent(register_user.this,login.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(register_user.this,"You are not Registered! Try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}

