package com.example.wuntu.billstore.Pojos;

/**
 * Created by Dell on 24-02-2018.
 */

public class CustomerDetails
{
    private String customerName;

    private String customerAddress;

    private String customerGstNumber;

    public CustomerDetails(String customerName, String customerAddress, String customerGstNumber) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerGstNumber = customerGstNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerGstNumber() {
        return customerGstNumber;
    }
}
