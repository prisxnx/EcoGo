package com.sp.navdrawertest;
public class MarkerData {
    private String userId;
    private double latitude;
    private double longitude;
    private String title;

    public MarkerData() {
    }

    public MarkerData(String userId, double latitude, double longitude, String title) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}