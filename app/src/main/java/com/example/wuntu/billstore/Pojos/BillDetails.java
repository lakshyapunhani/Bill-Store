package com.example.wuntu.billstore.Pojos;

import java.lang.reflect.Array;
import java.util.Map;

/**
 * Created by Dell on 1/15/2018.
 */

public class BillDetails
{
    private String billAmount;

    private String billDescription;

    private String billDate;

    private String billStatus;

    private Map<String,String> billImages;

    //private String[] billImages;

    public BillDetails() {}



    public BillDetails(String billAmount, String billDescription, String billDate, String billStatus, Map<String,String> billImages) {
        this.billAmount = billAmount;
        this.billDescription = billDescription;
        this.billDate = billDate;
        this.billStatus = billStatus;
        this.billImages = billImages;
    }

    public String getBillAmount() {
        return billAmount;
    }

    /*public String[] getBillImages() {
        return billImages;
    }*/
    public Map<String, String> getBillImages() {
        return billImages;
    }

    public String getBillDescription() {
        return billDescription;
    }

    public String getBillDate() {
        return billDate;
    }

    public String getBillStatus() {
        return billStatus;
    }
}
