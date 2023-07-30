package com.example.mybudgettrack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mybudgettrack.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.annotations.NonNull;

public class register_user extends AppCompatActivity {
    Button btnDaftar,btnLogin;
    EditText etEmail, etPassword,etUserName;
    FirebaseAuth mAuth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        etEmail=findViewById(R.id.email);
        etPassword=findViewById(R.id.password);
        etUserName=findViewById(R.id.username);
        btnDaftar=findViewById(R.id.daftar_button);
        btnLogin= findViewById(R.id.login_button);
        mAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");


        btnLogin.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View view) {
                intent= new Intent(register_user.this,login.class);
                startActivity(intent);
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();
                final String username = etUserName.getText().toString().trim();

                if (email.isEmpty()) {
                    etEmail.setError("Email is empty");
                    etEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Enter a valid email address");
                    etEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    etPassword.setError("Enter the password");
                    etPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    etPassword.setError("Length of the password should be at least 6");
                    etPassword.requestFocus();
                    return;
                }

                if (username.isEmpty()) {
                    etUserName.setError("Enter the password");
                    etUserName.requestFocus();
                    return;
                }

                if (username.length() < 6) {
                    etUserName.setError("Length of the username should be at least 6");
                    etUserName.requestFocus();
                    return;
                }

                // Register the user with Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Get the authenticated user's ID
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    String userID = firebaseUser.getUid();

                                    // Create a new User instance
                                    User user = new User(userID, email, username, 0.0, 0.0, 0.0,true);

                                    // Insert the user into Firestore
                                    usersCollection.document(userID).set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Registration and data insertion successful
                                                    Toast.makeText(register_user.this, "You are successfully registered", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(register_user.this, login.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Data insertion failed
                                                    Toast.makeText(register_user.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    // Registration failed
                                    Toast.makeText(register_user.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }
}

