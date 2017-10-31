package com.example.wuntu.billstore.Utils;

/**
 * Created by Wuntu on 31-10-2017.
 */

public class User
{
    private String name;
    private String shop_name;

    public User() {}

    public User(String name, String shop_name) {
        this.name = name;
        this.shop_name = shop_name;
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
}

