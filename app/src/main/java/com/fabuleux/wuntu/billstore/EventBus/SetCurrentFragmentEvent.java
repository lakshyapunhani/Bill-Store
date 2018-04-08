package com.fabuleux.wuntu.billstore.EventBus;

/**
 * Created by Dell on 18-03-2018.
 */

public class SetCurrentFragmentEvent
{
    String show = "";
    String hide = "";
    String hide2 = "";
    String hide3 = "";

    public SetCurrentFragmentEvent(String show, String hide, String hide2, String hide3) {
        this.show = show;
        this.hide = hide;
        this.hide2 = hide2;
        this.hide3 = hide3;
    }

    public String getShow() {
        return show;
    }

    public String getHide() {
        return hide;
    }

    public String getHide2() {
        return hide2;
    }

    public String getHide3() {
        return hide3;
    }
}
