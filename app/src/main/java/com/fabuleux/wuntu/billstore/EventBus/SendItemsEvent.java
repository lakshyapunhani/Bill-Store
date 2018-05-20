package com.fabuleux.wuntu.billstore.EventBus;

import com.fabuleux.wuntu.billstore.Pojos.ItemSelectionPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roadcast on 27/4/18.
 */

public class SendItemsEvent
{
    List<ItemSelectionPojo> itemList;

    String flag;


    public SendItemsEvent(List<ItemSelectionPojo> itemList, String flag)
    {
        this.itemList = itemList;
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<ItemSelectionPojo> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<ItemSelectionPojo> itemList) {
        this.itemList = itemList;
    }
}
