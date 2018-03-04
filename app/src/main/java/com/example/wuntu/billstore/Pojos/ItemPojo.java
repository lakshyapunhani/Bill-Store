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

    private String itemType;

    private String totalAmount;

    private String note;


    public ItemPojo(){}

    public ItemPojo(String itemName, String costPerItem, String quantity,String itemType, String totalAmount,String note) {
        this.itemName = itemName;
        this.costPerItem = costPerItem;
        this.quantity = quantity;
        this.itemType = itemType;
        this.totalAmount = totalAmount;
        this.note = note;
    }

    protected ItemPojo(Parcel in) {
        itemName = in.readString();
        costPerItem = in.readString();
        quantity = in.readString();
        itemType = in.readString();
        totalAmount = in.readString();
        note = in.readString();
    }

    public static final Creator<ItemPojo> CREATOR = new Creator<ItemPojo>() {
        @Override
        public ItemPojo createFromParcel(Parcel in) {
            return new ItemPojo(in);
        }

        @Override
        public ItemPojo[] newArray(int size) {
            return new ItemPojo[size];
        }
    };

    public String getNote() { return note;}

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

    public void setItemType(String itemType) { this.itemType = itemType;}

    public String getItemType(){ return itemType; }

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
        parcel.writeString(itemType);
        parcel.writeString(totalAmount);
        parcel.writeString(note);
    }
}
