package com.example.wuntu.billstore.Pojos;

import java.lang.reflect.Array;
import java.util.Map;

/**
 * Created by Dell on 1/15/2018.
 */

public class AddBillDetails
{
    private String vendorName;

    private String vendorAddress;

    private String billAmount;

    private String billDescription;

    private String billDate;

    private String billStatus;

    private String billTime;

    private Map<String,String> billImages;

    public AddBillDetails() {}

    public AddBillDetails(String newVendorName, String newVendorAddress, String billAmount, String billDescription, String billDate, String billStatus, Map<String,String> billImages, String billTime) {
        this.vendorName = newVendorName;
        this.vendorAddress = newVendorAddress;
        this.billAmount = billAmount;
        this.billDescription = billDescription;
        this.billDate = billDate;
        this.billStatus = billStatus;
        this.billImages = billImages;
        this.billTime = billTime;
    }

    public String getBillAmount() {
        return billAmount;
    }

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

    public String getVendorName() { return vendorName; }

    public String getVendorAddress() { return vendorAddress; }

    public String getBillTime() { return billTime; }
}
