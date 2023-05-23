package com.example.mybudgettrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgettrack.Adapter.BilAdapter;
import com.example.mybudgettrack.Model.BilModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BilListActivity extends AppCompatActivity {

    List<BilModel> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    //layout manager for recycler view
    RecyclerView.LayoutManager layoutManager;
    //firestore instance
    FirebaseFirestore db;
    BilAdapter adapter;
    ProgressDialog pd;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bil_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("List Data");
        //init firestore
        db = FirebaseFirestore.getInstance();


        //initialize view
        mRecyclerView=findViewById(R.id.recycler_view);
        floatingActionButton=findViewById(R.id.fab_button);
        //set recycler view properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BilListActivity.this,Bil.class));
            }
        });

        pd = new ProgressDialog(this);
        //show data in recycler view
        showData();

    }

    private void showData() {
        //set title of progress dialog
        pd.setTitle("Loading data....");
        pd.show();

        db.collection("bil1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        modelList.clear();
                        pd.dismiss();



                        for(DocumentSnapshot doc: task.getResult()){
                            BilModel model = new BilModel(
                                    doc.getString("id")
                                    ,doc.getString("Wang perbelanjaan")
                                    ,doc.getString("Tarikh perbelanjaan")
                                    ,doc.getString("Penerangan perbelanjaan"));


                            modelList.add(model);
                        }

                        //adapter
                        adapter = new BilAdapter(BilListActivity.this,modelList);
                        //set adapter to recycler view
                        mRecyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(BilListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteData(int index){
        //set title of progress dialog
        pd.setTitle("Deleting data....");
        pd.show();
        db.collection("bil1").document(modelList.get(index).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(BilListActivity.this, "Deleted ...", Toast.LENGTH_SHORT).show();
                        showData();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(BilListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}