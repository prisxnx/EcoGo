package com.sp.navdrawertest;

public class Helper {
    String profile,name, password, confirm;

    public Helper(String profile, String name, String password, String confirm) {
        this.profile = profile;
        this.name = name;
        this.password = password;
        this.confirm = confirm;
    }

    // Empty constructor (required by Firebase)
    public Helper() {
    }

    // Getters and setters...
    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}