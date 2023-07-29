package com.example.mybudgettrack;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BilListActivity extends AppCompatActivity {

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    List<BilModel> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    //layout manager for recycler view
    RecyclerView.LayoutManager layoutManager;
    //firestore instance
    FirebaseFirestore db;
    BilAdapter adapter;
    ProgressDialog pd;
    FloatingActionButton floatingActionButton;

    private static final String CHANNEL_ID = "PaymentChannel";
    // Notification ID
    private static final int NOTIFICATION_ID = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bil_list);

        showPopup();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Senarai Bil");
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
    private void showPopup() {
        // Inflate the popup layout
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.bil_popup_layout, null);



        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView)
                .setPositiveButton("Baik", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle the "OK" button click if needed
                    }
                });

        // Create and show the AlertDialog
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    private void showData() {
        //set title of progress dialog
        pd.setTitle("Sedang memuat turun senarai bil....");
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
                                    ,doc.getString("Wang pembayaran")
                                    ,doc.getString("Tarikh bayar")
                                    ,doc.getString("Penerangan bil"));


                            modelList.add(model);
                        }

                        //adapter
                        adapter = new BilAdapter(BilListActivity.this,modelList);
                        //set adapter to recycler view
                        mRecyclerView.setAdapter(adapter);

                        // Check for notifications
                        checkForNotifications();
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
        pd.setTitle("Membuang bil....");
        pd.show();
        db.collection("bil1").document(modelList.get(index).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(BilListActivity.this, "Sudah dibuang ...", Toast.LENGTH_SHORT).show();
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


    private void checkForNotifications() {
        // Get the current date
        Date currentDate = Calendar.getInstance().getTime();
        // Format the current date to match the date format in Firestore ("yyyy-MM-dd")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = dateFormat.format(currentDate);

        // Check for matching "Tarikh perbelanjaan" in the modelList
        for (BilModel model : modelList) {
            if (model.getTarikhBayar().equals(currentDateString)) {
                // Show in-app notification
                showBilNotification(Double.parseDouble(model.getWangBil()),model.getTarikhBayar());
                break; // No need to continue checking once a match is found
            }
        }
    }


    private void showBilNotification(double paymentMoney, String dateOfSpend) {

        String message = "Anda harus membayar sebanyak RM" + paymentMoney+" pada "+dateOfSpend;

        // Create a notification channel (required for newer Android versions)
        createNotificationChannel();

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Peringatan pembayaran bil")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Payment Channel";
            String description = "Channel for bil payment notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onBackPressed() {
        // Handle the back button press
        Intent intent = new Intent(BilListActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: If you don't want to keep the SecondActivity in the back stack
    }
}