package com.example.mybudgettrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybudgettrack.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    RelativeLayout btnBajet,btnSetBajet,btnBil,btnGraf;
    TextView tvDailyExp,tvSavingGoal,tvSaving;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "MyPrefs";
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    LinearLayout parentLayout;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        btnBajet = findViewById(R.id.btnBajet);
        btnSetBajet=findViewById(R.id.btnSetBajet);
        btnBil=findViewById(R.id.btnBil);
        btnGraf=findViewById(R.id.btnGraf);

        String userId = mAuth.getCurrentUser().getUid();


        // Find the parent layout
         parentLayout = findViewById(R.id.parentLayout);

        // Find the TextView elements within the parent layout
        tvDailyExp = parentLayout.findViewById(R.id.tvDailyExp);
        tvSavingGoal = parentLayout.findViewById(R.id.tvSavingGoal);
        tvSaving = parentLayout.findViewById(R.id.tvSaving);


//        tvDailyExp=findViewById(R.id.tvDailyExp);
//        tvSavingGoal=findViewById(R.id.tvSavingGoal);


        preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        btnSetBajet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SetBajet.class));
            }
        });

        btnBajet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,BajetListActivity.class));
            }
        });

        btnBil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,BilListActivity.class));
            }
        });

        btnGraf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserListActivity.class));
            }
        });



    }



    private void getUserData(String userId) {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        User user = document.toObject(User.class);
                        String userName = user.getUserName();
                        Double userDailyExp=user.getUserDailyExpenses();
                        Double userSavingGoal=user.getUserSavingGoal();
                        Double userSaving= user.getUserTotalSaving();
                        // Use the userName as needed
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setTitle("Welcome, "+userName);

                        // Use the other variable as needed
                        tvDailyExp.setText("RM "+String.valueOf(decimalFormat.format(userDailyExp)));
                        tvSavingGoal.setText("Saving goal: RM "+String.valueOf(decimalFormat.format(userSavingGoal)));
                        tvSaving.setText("Saving : RM "+String.valueOf(decimalFormat.format(userSaving)));

                    }
                } else {
                    Log.d("Firestore", "Error getting user document: " + task.getException());
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout_item) {
            mAuth.signOut();
            Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
        }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not signed in, redirect to login activity
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
            finish();
        } else {
            // User is signed in, retrieve user data
            String userId = mAuth.getCurrentUser().getUid();
            getUserData(userId);
        }
    }

}


