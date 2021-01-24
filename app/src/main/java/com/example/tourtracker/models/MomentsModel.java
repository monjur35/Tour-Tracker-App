package com.example.tourtracker.models;

public class MomentsModel {
    public String momentId;
    public String tourid;
    public String imageName;
    public long timeStamp;
    public String imageDownloadUrl;

    public MomentsModel() {
    }

    public MomentsModel(String momentId, String tourid, String imageName, long timeStamp, String imageDownloadUrl) {
        this.momentId = momentId;
        this.tourid = tourid;
        this.imageName = imageName;
        this.timeStamp = timeStamp;
        this.imageDownloadUrl = imageDownloadUrl;
    }
}
