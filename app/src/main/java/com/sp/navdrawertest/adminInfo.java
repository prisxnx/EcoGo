package com.sp.navdrawertest;

public class adminInfo {
    private String LocationLongitude,LocationLatitude,PostCaption,PostImage,PostSiteName,PostState;
    private int type;

    public adminInfo() {

    }

    public adminInfo(String postSiteName, String postCaption, String postState,
                    String locationLongitude, String locationLatitude,String postImage) {
        PostSiteName = postSiteName;
        PostCaption = postCaption;
        PostState = postState;
        LocationLongitude = locationLongitude;
        LocationLatitude = locationLatitude;
        PostImage = postImage;
    }

    public int getType() {
        return type;
    }

    public void setType() {
        this.type=type;
    }

    public String getPostSiteName() {
        return PostSiteName;
    }

    public void setPostSiteName(String postSiteName) {
        PostSiteName = postSiteName;
    }

    public String getPostCaption() {
        return PostCaption;
    }

    public void setPostCaption(String postCaption) {
        PostCaption = postCaption;
    }

    public String getPostState() {
        return PostState;
    }

    public void setPostState(String postState) {
        PostState = postState;
    }

    public String getLocationLongitude() {
        return LocationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        LocationLongitude = locationLongitude;
    }

    public String getLocationLatitude() {
        return LocationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        LocationLatitude = locationLatitude;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

}
