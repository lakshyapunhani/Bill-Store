package com.example.wuntu.billstore.Pojos;

/**
 * Created by Dell on 1/15/2018.
 */

public class BillDetails
{
    private String billAmount;

    private String billDescription;

    private String billDate;

    private String billStatus;

    public BillDetails() {}

    public BillDetails(String billAmount, String billDescription, String billDate,String billStatus) {
        this.billAmount = billAmount;
        this.billDescription = billDescription;
        this.billDate = billDate;
        this.billStatus = billStatus;
    }

    public String getBillAmount() {
        return billAmount;
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
