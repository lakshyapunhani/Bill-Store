package com.fabuleux.wuntu.billstore.Pojos;

/**
 * Created by Wuntu on 31-10-2017.
 */

public class User
{
    private String name;
    private String shop_name;
    private String shop_address;
    private String shop_gst;
    private String shop_pan;

    public User() {}

    public User(String name, String shop_name,String shop_address,String shop_gst,String shop_pan) {
        this.name = name;
        this.shop_name = shop_name;
        this.shop_address = shop_address;
        this.shop_gst = shop_gst;
        this.shop_pan = shop_pan;
    }

    public String getShop_address() {
        return shop_address;
    }

    public String getShop_gst() {
        return shop_gst;
    }

    public String getShop_pan() {
        return shop_pan;
    }

    public String getName() {
        return name;
    }

    public String getShop_name() {
        return shop_name;
    }

}

