package com.fabuleux.wuntu.billstore.EventBus;

import com.fabuleux.wuntu.billstore.Pojos.ExtraDetailsPojo;
import com.fabuleux.wuntu.billstore.Pojos.ItemSelectionPojo;

import java.util.ArrayList;
import java.util.List;

public class SendExtraDetails
{
    ExtraDetailsPojo extraDetailsPojo;

    String flag;


    public SendExtraDetails(ExtraDetailsPojo extraDetailsPojo, String flag)
    {
        this.extraDetailsPojo = extraDetailsPojo;
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public ExtraDetailsPojo getExtraDetailsPojo() {
        return extraDetailsPojo;
    }

    public void setExtraDetailsPojo(ExtraDetailsPojo extraDetailsPojo) {
        this.extraDetailsPojo = extraDetailsPojo;
    }

}
