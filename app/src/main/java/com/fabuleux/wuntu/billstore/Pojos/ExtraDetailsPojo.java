package com.fabuleux.wuntu.billstore.Pojos;

public class ExtraDetailsPojo
{
    int cgst;
    int sgst;
    int igst;
    int utgst;
    int shipping_charges;
    int discount;
    boolean roundOff;

    public int getCgst() {
        return cgst;
    }

    public void setCgst(int cgst) {
        this.cgst = cgst;
    }

    public int getSgst() {
        return sgst;
    }

    public void setSgst(int sgst) {
        this.sgst = sgst;
    }

    public int getIgst() {
        return igst;
    }

    public void setIgst(int igst) {
        this.igst = igst;
    }

    public int getUtgst() {
        return utgst;
    }

    public void setUtgst(int utgst) {
        this.utgst = utgst;
    }

    public int getShipping_charges() {
        return shipping_charges;
    }

    public void setShipping_charges(int shipping_charges) {
        this.shipping_charges = shipping_charges;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public boolean isRoundOff() {
        return roundOff;
    }

    public void setRoundOff(boolean roundOff) {
        this.roundOff = roundOff;
    }
}
