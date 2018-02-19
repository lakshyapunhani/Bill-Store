package com.example.wuntu.billstore.Pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roadcast on 19/2/18.
 */

public class ItemPojo implements Parcelable
{

    private String itemName;

    private String costPerItem;

    private String quantity;

    private String totalAmount;


    public ItemPojo(){}

    public ItemPojo(String itemName, String costPerItem, String quantity, String totalAmount) {
        this.itemName = itemName;
        this.costPerItem = costPerItem;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    protected ItemPojo(Parcel in) {
        itemName = in.readString();
        costPerItem = in.readString();
        quantity = in.readString();
        totalAmount = in.readString();
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCostPerItem() {
        return costPerItem;
    }

    public void setCostPerItem(String costPerItem) {
        this.costPerItem = costPerItem;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemName);
        parcel.writeString(costPerItem);
        parcel.writeString(quantity);
        parcel.writeString(totalAmount);
    }
}
