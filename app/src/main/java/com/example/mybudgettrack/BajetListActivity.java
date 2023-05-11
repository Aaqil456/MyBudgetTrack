package com.example.mybudgettrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mybudgettrack.Adapter.BajetAdapter;
import com.example.mybudgettrack.Model.BajetModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tflite.acceleration.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BajetListActivity extends AppCompatActivity {

    List<BajetModel> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    //layout manager for recycler view
    RecyclerView.LayoutManager layoutManager;
    //firestore instance
    FirebaseFirestore db;
    BajetAdapter adapter;
    ProgressDialog pd;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajet_list);
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
                startActivity(new Intent(BajetListActivity.this,Bajet.class));
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
        
        db.collection("bajet1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    modelList.clear();
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

    public void deleteData(int index){
        //set title of progress dialog
        pd.setTitle("Deleting data....");
        pd.show();
        db.collection("bajet1").document(modelList.get(index).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(BajetListActivity.this, "Deleted ...", Toast.LENGTH_SHORT).show();
                        showData();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(BajetListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

/*
    private void searchData(String s) {
        pd.setTitle("Cari...");
        pd.show();

        db.collection("bajet1").whereEqualTo("search",s.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        modelList.clear();
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
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(BajetListActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflating main_menu.xml
        getMenuInflater().inflate(R.menu.main_menu, menu);

        //search view
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView =  (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //called when press search button
                searchData(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called when we are typing
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    
    }

    */
}