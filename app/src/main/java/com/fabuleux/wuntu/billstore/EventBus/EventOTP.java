package com.fabuleux.wuntu.billstore.EventBus;

/**
 * Created by Dell on 12/27/2017.
 */

public class EventOTP
{
    private String code;

    public EventOTP(String code)
    {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
