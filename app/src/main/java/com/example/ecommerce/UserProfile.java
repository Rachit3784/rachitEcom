package com.example.ecommerce;

import com.google.firebase.Timestamp;

public class UserProfile {
    private String UId;
    private String name;
    private String murl;

    public UserProfile(){

    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMurl() {
        return murl;
    }

    public void setMurl(String murl) {
        this.murl = murl;
    }
}
