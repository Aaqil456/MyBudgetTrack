package com.example.mybudgettrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mybudgettrack.Adapter.BajetAdapter;
import com.example.mybudgettrack.Model.BajetModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class BajetListActivity extends AppCompatActivity {

    List<BajetModel> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    //layout manager for recycler view
    RecyclerView.LayoutManager layoutManager;
    //firestore instance
    FirebaseFirestore db;
    BajetAdapter adapter;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajet_list);
        //init firestore
        db = FirebaseFirestore.getInstance();

        //initialize view
        mRecyclerView=findViewById(R.id.recycler_view);

        //set recycler view properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        pd = new ProgressDialog(this);
        //show data in recycler view
        showData();
    }

    private void showData() {
        //set title of progress dialog
        pd.setTitle("Loading data....");
        pd.show();
        
        db.collection("bajet1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    pd.dismiss();
                    for(DocumentSnapshot doc: task.getResult()){
                        BajetModel model = new BajetModel(doc.getString("id")
                                ,doc.getString("Wang perbelanjaan")
                                ,doc.getString("Tarikh perbelanjaan")
                                ,doc.getString("Penerangan perbelanjaan"));
                            modelList.add(model);
                         }
                    //adapter
                    adapter = new BajetAdapter(BajetListActivity.this,modelList);
                    //set adapter to recycler view
                    mRecyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                        Toast.makeText(BajetListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}