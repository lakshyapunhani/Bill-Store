package com.example.wuntu.billstore.Pojos;

import java.util.Map;

/**
 * Created by Dell on 24-02-2018.
 */

public class MakeBillDetails
{
    /*private String customerName;

    private String customerAddress;

    private String customerGstNumber;*/



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

    /*public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerGstNumber() {
        return customerGstNumber;
    }*/

    public String getBillTime() {
        return billTime;
    }

    public Map<String, ItemPojo> getBillItems() {
        return billItems;
    }
}
