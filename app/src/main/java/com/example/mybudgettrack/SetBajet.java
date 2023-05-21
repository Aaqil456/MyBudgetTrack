package com.example.mybudgettrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class SetBajet extends AppCompatActivity {
    private EditText budgetInput;
    private RadioGroup timePeriodSelection;
    private Button calculateButton;
    private TextView resultDisplay;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_bajet);

        // Initialize views
        budgetInput = findViewById(R.id.edittext_budget);
        timePeriodSelection = findViewById(R.id.radiogroup_duration);
        calculateButton = findViewById(R.id.button_calculate);
        resultDisplay = findViewById(R.id.result_display);

        double dailyExpenditure;

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
                int checkedRadioButtonId = timePeriodSelection.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    resultDisplay.setText("Please select a time period.");
                    return;
                }
                RadioButton checkedRadioButton = findViewById(checkedRadioButtonId);
                String timePeriod = checkedRadioButton.getText().toString();

                // Calculate daily expenditure
                double dailyExpenditure;
                if (timePeriod.equals("Weekly")) {
                    dailyExpenditure = budget / 7;
                } else {
                    dailyExpenditure = budget / 30;
                }

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
}