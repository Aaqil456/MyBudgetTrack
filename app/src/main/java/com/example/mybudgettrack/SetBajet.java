package com.example.mybudgettrack;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.rxjava3.annotations.NonNull;

public class SetBajet extends AppCompatActivity {
    private EditText budgetInput,savingInput;
    private Button calculateButton;
    private TextView resultDisplay;
    private TextView btnSetStartDate, btnSetEndDate,tvStartDate,tvEndDate;
    private Calendar startDate, endDate;
    private FirebaseFirestore db;


    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_bajet);

        // Initialize views
        budgetInput = findViewById(R.id.edittext_budget);
        savingInput = findViewById(R.id.edittext_saving);

        calculateButton = findViewById(R.id.button_calculate);
        resultDisplay = findViewById(R.id.result_display);
        btnSetStartDate = findViewById(R.id.btnSetStartDate);
        btnSetEndDate = findViewById(R.id.btnSetEndDate);
        tvStartDate = findViewById(R.id.btnSetStartDate1);
        tvEndDate = findViewById(R.id.btnSetEndDate1);
        // Initialize FirebaseFirestore
        db = FirebaseFirestore.getInstance();





        btnSetStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true);
            }
        });

        btnSetEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
            }
        });



        // Set click listener for calculate button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get budget input
                String budgetString = budgetInput.getText().toString();
                if (budgetString.isEmpty()) {

                    resultDisplay.setText("isi semua tempat kosong");
                    return;
                }
                double budget = Double.parseDouble(budgetString);

                // Calculate daily expenditure
                double dailyExpenditure=budget/calculateNumberOfDays();
                String dailyExpenditure_str=String.valueOf(dailyExpenditure);

                // Get saving input
                String savingString = savingInput.getText().toString();

                double saving = Double.parseDouble(savingString)/calculateNumberOfDays();

                // Set the dailyExpenditure in the Singleton
                DailyExpenditureSingleton.getInstance().setDailyExpenditure(dailyExpenditure);

                // Display result
                String result = "Perbelanjaan harian anda adalah RM" + decimalFormat.format(dailyExpenditure);
                resultDisplay.setText(result);

                // Update the userDailyExpenditure in Firestore
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference userRef = db.collection("users").document(userId);
                userRef.update("userDailyExpenses", dailyExpenditure,"userSavingGoal",saving)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Value updated successfully
                                Toast.makeText(SetBajet.this, "Daily expenditure updated in Firestore", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Value update failed
                                Toast.makeText(SetBajet.this, "Failed to update daily expenditure: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

    }



    public void setStartDate(View view) {
        showDatePickerDialog(true);
    }

    public void setEndDate(View view) {
        showDatePickerDialog(false);
    }

    public int calculateNumberOfDays() {
        if (startDate != null && endDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String startDateString = format.format(startDate.getTime());
            String endDateString = format.format(endDate.getTime());

            try {
                Date startDate = format.parse(startDateString);
                Date endDate = format.parse(endDateString);

                long difference = endDate.getTime() - startDate.getTime();
                int numberOfDays = (int) (difference / (1000 * 60 * 60 * 24));

                return numberOfDays;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return 0; // Return 0 if start or end date is null or in case of any error
    }


    private void showDatePickerDialog(final boolean isStartDate) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);

                        if (isStartDate) {
                            startDate = selectedDate;
                            tvStartDate.setText(formatDate(selectedDate));
                        } else {
                            endDate = selectedDate;
                            tvEndDate.setText(formatDate(selectedDate));
                        }
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private String formatDate(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calendar.getTime());
    }

    @Override
    public void onBackPressed() {
        // Handle the back button press
        Intent intent = new Intent(SetBajet.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: If you don't want to keep the SecondActivity in the back stack
    }
}