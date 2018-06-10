package com.fabuleux.wuntu.billstore.Pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by Dell on 24-02-2018.
 */

public class MakeBillDetails
{

    private CustomerDetails customerDetails;

    private String billTime;

    private double cgst;

    private double sgst;

    private double igst;

    private double gstRate;

    private double billAmount;

    private String invoiceNumber;

    private Map<String,ItemPojo> billItems;

    public MakeBillDetails()
    {}

    public MakeBillDetails(CustomerDetails customerDetails, String billTime,double cgst,double sgst,double igst,double gstRate,  Map<String, ItemPojo> billItems,double billAmount,String invoiceNumber) {
        this.customerDetails = customerDetails;
        this.billTime = billTime;
        this.cgst = cgst;
        this.sgst = sgst;
        this.igst = igst;
        this.gstRate = gstRate;
        this.billItems = billItems;
        this.billAmount = billAmount;
        this.invoiceNumber = invoiceNumber;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public String getBillTime() {
        return billTime;
    }

    public Map<String, ItemPojo> getBillItems() {
        return billItems;
    }

    public String getInvoiceNumber(){ return invoiceNumber;}

    public double getGstRate() {
        return gstRate;
    }

    public void setGstRate(double gstRate) {
        this.gstRate = gstRate;
    }

    public double getIgst() {
        return igst;
    }

    public void setIgst(double igst) {
        this.igst = igst;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }
}
