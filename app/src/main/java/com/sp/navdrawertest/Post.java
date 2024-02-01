package com.sp.navdrawertest;

public class Post {

    private String caption;
    private String place;
    private String state;
    private String dateOfVisit;
    private String timestamp;


    // Required default constructor for Firebase
    public Post() {
    }

    public Post(String caption, String place, String state, String dateOfVisit) {
        this.caption = caption;
        this.place = place;
        this.state = state;
        this.dateOfVisit = dateOfVisit;

    }
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}
