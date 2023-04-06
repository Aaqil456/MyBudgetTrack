package com.example.mybudgettrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Bil extends AppCompatActivity {
    Button btnAddBil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bil);

        btnAddBil=findViewById(R.id.buttonNext);

        btnAddBil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Bil.this, CiptaBil.class));

            }
        });
    }
}