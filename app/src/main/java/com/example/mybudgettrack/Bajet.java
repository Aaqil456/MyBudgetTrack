package com.example.mybudgettrack;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mybudgettrack.Adapter.BajetAdapter;
import com.example.mybudgettrack.Item.BajetItem;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class Bajet extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BajetAdapter mAdapter;
    private List<BajetItem> mData;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajet);

        btnAdd=findViewById(R.id.add_button);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mData = new ArrayList<>();
        mAdapter = new BajetAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);

        FirebaseFirestore.getInstance().collection("bajets")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        BajetItem model = document.toObject(BajetItem.class);
                        mData.add(model);
                    }
                    mAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Bajet.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error retrieving data", e);
                });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Bajet.this, CiptaBajet.class));
               }
            });

    }
}