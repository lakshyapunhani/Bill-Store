package com.fabuleux.wuntu.billstore.EventBus;

public class EventPrintOtp
{
    private String code1;
    private String code2;
    private String code3;
    private String code4;
    private String code5;
    private String code6;

    public EventPrintOtp(String code, String code2, String code3, String code4, String code5, String code6)
    {
        this.code1 = code;
        this.code2 = code2;
        this.code3 = code3;
        this.code4 = code4;
        this.code5 = code5;
        this.code6 = code6;
    }

    public String getCode1() {
        return code1;
    }

    public String getCode2() { return code2; }

    public String getCode3() {
        return code3;
    }

    public String getCode4() {
        return code4;
    }

    public String getCode5() {
        return code5;
    }

    public String getCode6() {
        return code6;
    }

}
