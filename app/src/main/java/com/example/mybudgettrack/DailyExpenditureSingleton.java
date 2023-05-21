package com.example.mybudgettrack;

public class DailyExpenditureSingleton {
    private static DailyExpenditureSingleton instance;
    private double dailyExpenditure;

    private DailyExpenditureSingleton() {
        // Private constructor to prevent instantiation
    }

    public static DailyExpenditureSingleton getInstance() {
        if (instance == null) {
            instance = new DailyExpenditureSingleton();
        }
        return instance;
    }

    public double getDailyExpenditure() {
        return dailyExpenditure;
    }

    public void setDailyExpenditure(double expenditure) {
        dailyExpenditure = expenditure;
    }
}
