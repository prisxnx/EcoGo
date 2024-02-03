package com.sp.navdrawertest;

public class postInfo {
    private String PostSiteName, PostCaption, PostDOV, PostState, LocationLongitude, LocationLatitude, PostDocID, PostImage,CurrentUserID;

    public postInfo(String postSiteName, String postCaption, String postDOV, String postState,
                    String locationLongitude, String locationLatitude, String postDocID, String postImage, String currentUserID) {
        PostSiteName = postSiteName;
        PostCaption = postCaption;
        PostDOV = postDOV;
        PostState = postState;
        LocationLongitude = locationLongitude;
        LocationLatitude = locationLatitude;
        PostDocID = postDocID;
        PostImage = postImage;
        CurrentUserID = currentUserID;
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

    public String getPostDOV() {
        return PostDOV;
    }

    public void setPostDOV(String postDOV) {
        PostDOV = postDOV;
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

    public String getPostDocID() {
        return PostDocID;
    }

    public void setPostDocID(String postDocID) {
        PostDocID = postDocID;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getCurrentUserID() {
        return CurrentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        CurrentUserID = currentUserID;
    }
}
