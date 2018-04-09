package com.fabuleux.wuntu.billstore.EventBus;

/**
 * Created by sushant on 4/14/17.
 */

public class InternetStatus {

    private boolean status;
    private String msg;
    private String type;

    public InternetStatus(boolean status, String msg) {
        this.status=status;
        this.msg=msg;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
