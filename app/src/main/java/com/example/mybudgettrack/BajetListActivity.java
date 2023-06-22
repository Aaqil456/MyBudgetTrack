package com.example.mybudgettrack;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.example.mybudgettrack.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BajetListActivity extends AppCompatActivity {

    List<BajetModel> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    //layout manager for recycler view
    RecyclerView.LayoutManager layoutManager;
    //firestore instance
    FirebaseFirestore db;
    BajetAdapter adapter;
    ProgressDialog pd;
    FloatingActionButton floatingActionButton;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");


    private SharedPreferences preferences;

    private static final String CHANNEL_ID = "OverspendingChannel";
    // Notification ID
    private static final int NOTIFICATION_ID = 1;

    // Added variable for saving total
    private double savingTotal = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajet_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Senarai Perbelanjaan");
        //init firestore
        db = FirebaseFirestore.getInstance();

        //initialize prefrecense to hold daily expenditure value
        preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

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
    private double retrieveDailyExpenditure() {

       return 0.0;
    }

    private void showData() {
        //set title of progress dialog
        pd.setTitle("Sedang memuat turun senarai perbelanjaan....");
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
                        // Retrieve the dailyExpenditure value from SharedPreferences

                        // Get the current date
                        Date currentDate = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String currentDateString = dateFormat.format(currentDate);
                        // Calculate the total money spent for the same date and check for overspending
                        for (BajetModel model : modelList) {
                            String dateOfSpend = model.getTarikhBajet();
                            if (dateOfSpend.equals(currentDateString)) { // Compare with current date
                                if (totalMoneySpentByDate.containsKey(dateOfSpend)) {
                                    double totalMoneySpent = totalMoneySpentByDate.get(dateOfSpend);
                                    model.setTotalMoneySpent(totalMoneySpent);

                                    String userId = mAuth.getCurrentUser().getUid();
                                    DocumentReference userRef = db.collection("users").document(userId);
                                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document != null && document.exists()) {
                                                    User user = document.toObject(User.class);
                                                    String userName = user.getUserName();
                                                    Double userDailyExp=user.getUserDailyExpenses();
                                                    Double userSavingGoal=user.getUserSavingGoal();

                                                    if (totalMoneySpent > userDailyExp) {
                                                        showOverspendingNotification(totalMoneySpent, userDailyExp,dateOfSpend);
                                                    } else {

                                                        double overspentAmount = userDailyExp - totalMoneySpent;
                                                        savingTotal += overspentAmount;

                                                        // Update the userDailyExpenditure in Firestore
                                                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                        DocumentReference userRef = db.collection("users").document(userId);
                                                        userRef.update("userTotalSaving", overspentAmount)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        // Value updated successfully
                                                                        Toast.makeText(BajetListActivity.this, "Total Saving updated in Firestore", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@io.reactivex.rxjava3.annotations.NonNull Exception e) {
                                                                        // Value update failed
                                                                        Toast.makeText(BajetListActivity.this, "Failed to update Total Saving : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                        // Inside your activity or fragment
                                                        Toast.makeText(getApplicationContext(), "This is a Toast message " + savingTotal, Toast.LENGTH_SHORT).show();

                                                    }

                                                }
                                            } else {
                                                Log.d("Firestore", "Error getting user document: " + task.getException());
                                            }
                                        }
                                    });



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

    private void showOverspendingNotification(double totalMoneySpent,double dailyExpenditure, String dateOfSpend) {

        String message = "You have overspent " + decimalFormat.format((totalMoneySpent - dailyExpenditure)) + " on " + dateOfSpend;

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

    @Override
    public void onBackPressed() {
        // Handle the back button press
        Intent intent = new Intent(BajetListActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: If you don't want to keep the SecondActivity in the back stack
    }


}