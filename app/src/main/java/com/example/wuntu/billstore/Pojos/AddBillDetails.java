package com.example.wuntu.billstore.Pojos;

import java.lang.reflect.Array;
import java.util.Map;

/**
 * Created by Dell on 1/15/2018.
 */

public class AddBillDetails
{
    private VendorDetails vendorDetails;

    private String billAmount;

    private String billDescription;

    private String billDate;

    private String billStatus;

    private String billTime;

    private Map<String,String> billImages;

    private String billNumber;

    public AddBillDetails() {}

    public AddBillDetails(VendorDetails vendorDetails, String billAmount, String billDescription, String billDate, String billStatus, Map<String,String> billImages, String billTime,String billNumber) {
        this.vendorDetails = vendorDetails;
        this.billAmount = billAmount;
        this.billDescription = billDescription;
        this.billDate = billDate;
        this.billStatus = billStatus;
        this.billImages = billImages;
        this.billTime = billTime;
        this.billNumber = billNumber;
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

    public String getBillTime() { return billTime; }

    public VendorDetails getVendorDetails() {
        return vendorDetails;
    }

    public String getBillNumber() {return billNumber; }
}
