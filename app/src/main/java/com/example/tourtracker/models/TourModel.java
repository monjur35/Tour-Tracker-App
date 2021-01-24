package com.example.tourtracker.models;

public class TourModel {



    private String tourId;
    private String userId;
    private String tourName;
    private String destination;
    private double budget;
    private String  formattedDate;
    private  long timeStamp;
    private int month;
    private int year;
    private boolean isCompleted;


    public TourModel() {
    }

    public TourModel(String tourId, String userId,String tourName, String destination, double budget, String formattedDate, long timeStamp, int month, int year) {
        this.tourId = tourId;
        this.userId = userId;
        this.tourName = tourName;
        this.destination = destination;
        this.budget = budget;
        this.formattedDate = formattedDate;
        this.timeStamp = timeStamp;
        this.month = month;
        this.year = year;
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        return "TourModel{" +
                "id='" + tourId + '\'' +
                ", tourName='" + tourName + '\'' +
                ", destination='" + destination + '\'' +
                ", budget=" + budget +
                ", formattedDate='" + formattedDate + '\'' +
                ", timeStamp=" + timeStamp +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
