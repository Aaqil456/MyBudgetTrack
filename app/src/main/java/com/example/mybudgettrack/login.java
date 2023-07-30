package com.example.mybudgettrack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

Button btnDaftar;
    Button btnLogin;
    EditText etEmail, etPassword;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnDaftar = findViewById(R.id.daftar_button);

        etEmail=findViewById(R.id.email);
        etPassword=findViewById(R.id.password);
        btnLogin=findViewById(R.id.login_button);
        mAuth=FirebaseAuth.getInstance();

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View view) {
                intent= new Intent(login.this,register_user.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= etEmail.getText().toString().trim();
                String password=etPassword.getText().toString().trim();

                if(email.isEmpty())
                {
                    etEmail.setError("Email is empty");
                    etEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    etEmail.setError("Enter the valid email");
                    etEmail.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    etPassword.setError("Password is empty");
                    etPassword.requestFocus();
                    return;
                }
                if(password.length()<6)
                {
                    etPassword.setError("Length of password is more than 6");
                    etPassword.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        startActivity(new Intent(login.this, MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(login.this,
                                "Please Check Your login Credentials",
                                Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });

    }
    @Override
    public void onBackPressed() {
        // Handle the back button press
        Intent intent = new Intent(login.this, register_user.class);
        startActivity(intent);
        finish(); // Optional: If you don't want to keep the SecondActivity in the back stack
    }

}

