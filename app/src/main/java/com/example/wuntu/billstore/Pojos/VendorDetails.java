package com.example.wuntu.billstore.Pojos;

/**
 * Created by Dell on 1/15/2018.
 */

public class VendorDetails
{
    private String vendorName;

    private String vendorAddress;

    public VendorDetails() {}

    public VendorDetails(String vendorName, String vendorAddress) {
        this.vendorName = vendorName;
        this.vendorAddress = vendorAddress;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }
}
