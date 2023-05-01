package com.example.mybudgettrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Bajet extends AppCompatActivity {

    EditText etWang, etTarikh, etPenerangan;
    Button btnSimpan;
    ProgressDialog pd;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajet);
        //action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tambah Data");
        //initialize
        etWang=findViewById(R.id.etWang);
        etTarikh=findViewById(R.id.etTarikh);
        etPenerangan=findViewById(R.id.etPenerangan);
        btnSimpan=findViewById(R.id.saveBtn);

        //progress dialog
        pd = new ProgressDialog(this);
        //firestore
        db =  FirebaseFirestore.getInstance();

        //click button to add data
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wang = etWang.getText().toString().trim();
                String tarikh = etTarikh.getText().toString().trim();
                String penerangan = etPenerangan.getText().toString().trim();

                uploadData(wang,tarikh,penerangan);
            }
        });


    }

    private void uploadData(String wang, String tarikh, String penerangan) {
        pd.setTitle("Menambah data");
        pd.show();

        //random id for each data
        String id = UUID.randomUUID().toString();

        Map<String,Object> doc = new HashMap<>();
        doc.put("id",id);
        doc.put("Wang perbelanjaan", wang);
        doc.put("Tarikh perbelanjaan", tarikh);
        doc.put("Penerangan perbelanjaan", penerangan);

        //add this data
        db.collection("bajet1").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(Bajet.this,"Uploaded",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Bajet.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}