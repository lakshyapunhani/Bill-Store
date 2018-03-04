package com.example.wuntu.billstore.EventBus;

/**
 * Created by Dell on 19-02-2018.
 */

public class ItemToMakeBill
{
    public String itemName;

    public String costPerItem;

    public String quantity;

    public String totalAmount;

    public String itemType;

    public String note;

    public ItemToMakeBill(String itemName, String costPerItem, String quantity,String itemType, String totalAmount,String note) {
        this.itemName = itemName;
        this.costPerItem = costPerItem;
        this.quantity = quantity;
        this.itemType = itemType;
        this.totalAmount = totalAmount;
        this.note = note;
    }

    public String getNote(){return note;}

    public String getItemName() {
        return itemName;
    }

    public String getCostPerItem() {
        return costPerItem;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getItemType()
    {
        return itemType;
    }


}
