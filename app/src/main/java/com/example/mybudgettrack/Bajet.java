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

import com.example.mybudgettrack.Adapter.BajetAdapter;
import com.example.mybudgettrack.Item.BajetItem;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class Bajet extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BajetAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference itemRef = db.collection("bajets");
    private ArrayList<BajetItem> mExampleList;
    List<BajetItem> items = new ArrayList<>();

    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajet);

        btnAdd=findViewById(R.id.add_button);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*
        items.add(new BajetItem("Bajet 1", "2000","2 bulan"));
        items.add(new BajetItem("Bajet 2", "12334","3 hari"));
        items.add(new BajetItem("Bajet 3", "12332","4 tahun"));
        */
        adapter = new BajetAdapter(items);
        recyclerView.setAdapter(adapter);


        itemRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }

                items.clear();
                for (DocumentSnapshot document : value) {
                    BajetItem bajetItem = document.toObject(BajetItem.class);
                    items.add(bajetItem);
                }
                adapter.notifyDataSetChanged();
            }
        });



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Bajet.this,CiptaBajet.class));
            }
        });
    }
}