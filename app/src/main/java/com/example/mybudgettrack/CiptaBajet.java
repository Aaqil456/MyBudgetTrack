package com.example.mybudgettrack;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CiptaBajet extends AppCompatActivity {
    FirebaseFirestore db;
    Button btnSubmit;
    EditText namaBajet,jumlahBajet,tempohBajet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cipta_bajet);
        namaBajet=findViewById(R.id.nama_bajet);
        jumlahBajet = findViewById(R.id.jumlah_bajet);
        tempohBajet=findViewById(R.id.tempoh_bajet);

        btnSubmit=findViewById(R.id.submit_button);

        db= FirebaseFirestore.getInstance();

        // Set an OnClickListener on your submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the data from the EditText views
                String nama = namaBajet.getText().toString();
                String jumlah = jumlahBajet.getText().toString();
                String tempoh = tempohBajet.getText().toString();


                // Create a HashMap to store your data
                Map<String, Object> bajet = new HashMap<>();
                bajet.put("nama", nama);
                bajet.put("jumlah", jumlah);
                bajet.put("tempoh", tempoh);


                // Add the data to Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference usersRef = db.collection("bajets");
                Task<DocumentReference> task = usersRef.add(bajet);

                // Handle the result of the task
                task.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(CiptaBajet.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(CiptaBajet.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}