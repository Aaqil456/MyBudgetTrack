package com.example.mybudgettrack;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SetBajet extends AppCompatActivity {
    private EditText budgetInput;
    private Button calculateButton;
    private TextView resultDisplay;
    private TextView btnSetStartDate, btnSetEndDate;
    private Calendar startDate, endDate;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_bajet);

        // Initialize views
        budgetInput = findViewById(R.id.edittext_budget);
        calculateButton = findViewById(R.id.button_calculate);
        resultDisplay = findViewById(R.id.result_display);
        btnSetStartDate = findViewById(R.id.btnSetStartDate);
        btnSetEndDate = findViewById(R.id.btnSetEndDate);

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
                    resultDisplay.setText("Please enter a budget amount.");
                    return;
                }
                double budget = Double.parseDouble(budgetString);

                // Get time period selection

                // Calculate daily expenditure
                double dailyExpenditure=budget/calculateNumberOfDays();

                /*
                if (timePeriod.equals("Week")) {
                    dailyExpenditure = budget / 7;
                } else {
                    dailyExpenditure = budget / 30;
                }*/

                // Set the dailyExpenditure in the Singleton
                DailyExpenditureSingleton.getInstance().setDailyExpenditure(dailyExpenditure);

                // Display result
                String result = "Your daily expenditure is RM" + decimalFormat.format(dailyExpenditure);
                resultDisplay.setText(result);

                // Save the dailyExpenditure value in SharedPreferences
                SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("dailyExpenditure", (float) dailyExpenditure);
                editor.apply();

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
                            btnSetStartDate.setText(formatDate(selectedDate));
                        } else {
                            endDate = selectedDate;
                            btnSetEndDate.setText(formatDate(selectedDate));
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