package com.example.wuntu.billstore.Pojos;

/**
 * Created by Wuntu on 31-10-2017.
 */

public class User
{
    private String name;
    private String shop_name;
    private String profileImageURL;

    public User() {}

    public User(String name, String shop_name,String profileImageURL) {
        this.name = name;
        this.shop_name = shop_name;
        this.profileImageURL = profileImageURL;
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}

