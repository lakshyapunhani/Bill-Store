package com.fabuleux.wuntu.billstore.Pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dell on 1/15/2018.
 */

public class VendorDetails implements Parcelable
{
    private String vendorName;

    private String vendorAddress;

    private String vendorGst;

    public VendorDetails() {}

    public VendorDetails(String vendorName, String vendorAddress,String vendorGst) {
        this.vendorName = vendorName;
        this.vendorAddress = vendorAddress;
        this.vendorGst = vendorGst;
    }

    protected VendorDetails(Parcel in) {
        vendorName = in.readString();
        vendorAddress = in.readString();
        vendorGst = in.readString();
    }

    public static final Creator<VendorDetails> CREATOR = new Creator<VendorDetails>() {
        @Override
        public VendorDetails createFromParcel(Parcel in) {
            return new VendorDetails(in);
        }

        @Override
        public VendorDetails[] newArray(int size) {
            return new VendorDetails[size];
        }
    };

    public String getVendorName() {
        return vendorName;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public String getVendorGst()
    {
        return vendorGst;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(vendorName);
        parcel.writeString(vendorAddress);
        parcel.writeString(vendorGst);
    }
}
