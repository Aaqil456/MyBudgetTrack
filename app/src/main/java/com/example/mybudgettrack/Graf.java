package com.example.mybudgettrack;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class Graf extends AppCompatActivity {
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graf);

        barChart = findViewById(R.id.barChart);

        // Fetch data from Firestore
        fetchDataFromFirestore();

    }

    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bajet1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        // Map to store the total money spent for each date
                        Map<String, Double> totalMoneySpentByDate = new HashMap<>();

                        for (DocumentSnapshot doc : task.getResult()) {
                            String date = doc.getString("Tarikh perbelanjaan");
                            String amountString = doc.getString("Wang perbelanjaan");
                            try {
                                double amount = Double.parseDouble(amountString);

                                // Update the total money spent for this date
                                if (totalMoneySpentByDate.containsKey(date)) {
                                    double currentTotal = totalMoneySpentByDate.get(date);
                                    totalMoneySpentByDate.put(date, currentTotal + amount);
                                } else {
                                    totalMoneySpentByDate.put(date, amount);
                                }
                            } catch (NumberFormatException e) {
                                // Handle the case where the string cannot be parsed to double
                                // For example, you can add a default value or skip this entry
                                e.printStackTrace();
                            }
                        }

                        // Create the bar entries
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        int index = 0;
                        for (String date : totalMoneySpentByDate.keySet()) {
                            double amount = totalMoneySpentByDate.get(date);
                            entries.add(new BarEntry(index++, (float) amount));
                        }

                        // Set the data to the BarChart
                        BarDataSet dataSet = new BarDataSet(entries, "Total Money Spent");
                        BarData barData = new BarData(dataSet);

                        BarChart barChart = findViewById(R.id.barChart);
                        barChart.setData(barData);

                        // Customize x-axis labels (dates)
                        final String[] dates = totalMoneySpentByDate.keySet().toArray(new String[0]);
                        ValueFormatter xAxisFormatter = new ValueFormatter() {
                            @Override
                            public String getAxisLabel(float value, AxisBase axis) {
                                int intValue = (int) value;
                                if (intValue >= 0 && intValue < dates.length) {
                                    return dates[intValue];
                                } else {
                                    return "";
                                }
                            }
                        };
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setGranularity(1f);
                        xAxis.setValueFormatter(xAxisFormatter);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                        // Refresh the chart
                        barChart.invalidate();
                    }
                });

    }

    

}