package com.example.tourtracker.models;

public class ExpenseModel {
    public String expId;
    public String tourId;
    public String title;
    public double amount;
    public long timeStamp;

    public ExpenseModel() {

    }

    public ExpenseModel(String expId, String title, double amount) {
        this.expId = expId;
        this.title = title;
        this.amount = amount;
    }

    public ExpenseModel(String expId, String tourId, String title, double amount, long timeStamp) {
        this.expId = expId;
        this.tourId = tourId;
        this.title = title;
        this.amount = amount;
        this.timeStamp = timeStamp;
    }
}
