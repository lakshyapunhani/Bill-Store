package com.example.wuntu.billstore.Pojos;

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

    private Map<String,ItemPojo> billItems;

    public MakeBillDetails(CustomerDetails customerDetails, String billTime, Map<String, ItemPojo> billItems) {
        this.customerDetails = customerDetails;
        this.billTime = billTime;
        this.billItems = billItems;
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

}
