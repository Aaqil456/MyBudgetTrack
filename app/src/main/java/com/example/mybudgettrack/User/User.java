package com.example.mybudgettrack.User;

public class User {
    private String userID;
    private String userEmail;
    private String userName;

    private double userDailyExpenses;
    private double userTotalSaving;
    private double userSavingGoal;

    public User(String userID, String userEmail, String userName, double userDailyExpenses, double userTotalSaving, double userSavingGoal) {
        this.userID = userID;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userDailyExpenses = userDailyExpenses;
        this.userTotalSaving = userTotalSaving;
        this.userSavingGoal = userSavingGoal;
    }

    public User() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getUserDailyExpenses() {
        return userDailyExpenses;
    }

    public void setUserDailyExpenses(double userDailyExpenses) {
        this.userDailyExpenses = userDailyExpenses;
    }

    public double getUserTotalSaving() {
        return userTotalSaving;
    }

    public void setUserTotalSaving(double userTotalSaving) {
        this.userTotalSaving = userTotalSaving;
    }

    public double getUserSavingGoal() {
        return userSavingGoal;
    }

    public void setUserSavingGoal(double userSavingGoal) {
        this.userSavingGoal = userSavingGoal;
    }
}

