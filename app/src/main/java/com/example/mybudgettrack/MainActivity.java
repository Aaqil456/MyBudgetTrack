package com.example.mybudgettrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    RelativeLayout btnBajet,btnSetBajet,btnBil,btnGraf;
    TextView tvDailyExp,tvSavingGoal,tvSaving;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "MyPrefs";
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        btnBajet = findViewById(R.id.btnBajet);
        btnSetBajet=findViewById(R.id.btnSetBajet);
        btnBil=findViewById(R.id.btnBil);
        btnGraf=findViewById(R.id.btnGraf);


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
                startActivity(new Intent(MainActivity.this,GraphActivity.class));
            }
        });

        float savedDailyExpenditure = preferences.getFloat("dailyExpenditure", 0.0f);
        float savingGoal = preferences.getFloat("Saving", 0.0f);
        float saving = preferences.getFloat("totalSaving", 0.0f);

        tvDailyExp.setText("RM "+String.valueOf(decimalFormat.format(savedDailyExpenditure)));
        tvSavingGoal.setText("Saving goal: RM "+String.valueOf(decimalFormat.format(savingGoal)));
        tvSaving.setText("Saving : RM "+String.valueOf(decimalFormat.format(saving)));



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

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
            finish();
        }
    }
    }


