package com.example.mybudgettrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgettrack.Adapter.UserAdapter;
import com.example.mybudgettrack.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class UserListActivity extends AppCompatActivity {
    List<User> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    //layout manager for recycler view
    RecyclerView.LayoutManager layoutManager;
    //firestore instance
    FirebaseFirestore db;
    UserAdapter adapter;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Senarai Pengguna");
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
        pd.setTitle("Sedang memuat turun senarai bil....");
        pd.show();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        modelList.clear();
                        pd.dismiss();



                        for (DocumentSnapshot doc : task.getResult()) {
                            User model = new User(
                                    doc.getString("userID"),
                                    doc.getString("userEmail"),
                                    doc.getString("userName"),
                                    doc.getDouble("userDailyExpenses") != null ? doc.getDouble("userDailyExpenses") : 0.0,
                                    doc.getDouble("userTotalSaving") != null ? doc.getDouble("userTotalSaving") : 0.0,
                                    doc.getDouble("userSavingGoal") != null ? doc.getDouble("userSavingGoal") : 0.0
                            );

                            modelList.add(model);
                        }


                        //adapter
                        adapter = new UserAdapter(UserListActivity.this,modelList);
                        //set adapter to recycler view
                        mRecyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(UserListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onBackPressed() {
        // Handle the back button press
        Intent intent = new Intent(UserListActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: If you don't want to keep the SecondActivity in the back stack
    }
}