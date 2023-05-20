package com.example.mybudgettrack;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudgettrack.Adapter.BajetAdapter;
import com.example.mybudgettrack.Model.BajetModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final String CHANNEL_ID = "OverspendingChannel";
    // Notification ID
    private static final int NOTIFICATION_ID = 1;
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

                        // Map to store the total money spent for each date
                        Map<String, Double> totalMoneySpentByDate = new HashMap<>();

                    for(DocumentSnapshot doc: task.getResult()){
                        BajetModel model = new BajetModel(
                                 doc.getString("id")
                                ,doc.getString("Wang perbelanjaan")
                                ,doc.getString("Tarikh perbelanjaan")
                                ,doc.getString("Penerangan perbelanjaan"));
                        // Get the date of spend
                        String dateOfSpend = doc.getString("Tarikh perbelanjaan");

                        // Calculate the total money spent for each date
                        double moneySpent = Double.parseDouble(doc.getString("Wang perbelanjaan"));
                        if (totalMoneySpentByDate.containsKey(dateOfSpend)) {
                            double currentTotal = totalMoneySpentByDate.get(dateOfSpend);
                            totalMoneySpentByDate.put(dateOfSpend, currentTotal + moneySpent);
                        } else {
                            totalMoneySpentByDate.put(dateOfSpend, moneySpent);
                        }

                            modelList.add(model);
                         }

                        // Calculate the total money spent for the same date and check for overspending
                        for (BajetModel model : modelList) {
                            String dateOfSpend = model.getTarikhBajet();
                            if (totalMoneySpentByDate.containsKey(dateOfSpend)) {
                                double totalMoneySpent = totalMoneySpentByDate.get(dateOfSpend);
                                model.setTotalMoneySpent(totalMoneySpent);

                                if (totalMoneySpent > 30) {
                                    showOverspendingNotification(totalMoneySpent, dateOfSpend);
                                }
                            }
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

    private void showOverspendingNotification(double totalMoneySpent, String dateOfSpend) {
        String message = "You have overspent " + (totalMoneySpent - 30) + " on " + dateOfSpend;

        // Create a notification channel (required for newer Android versions)
        createNotificationChannel();

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Overspending Alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Overspending Channel";
            String description = "Channel for overspending notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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