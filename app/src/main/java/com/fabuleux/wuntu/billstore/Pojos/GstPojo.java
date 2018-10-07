package com.fabuleux.wuntu.billstore.Pojos;

public class GstPojo
{
    private double sgst;

    private double igst;

    private double utgst;

    private double shippingCharges;

    private double discount;

    public GstPojo() {
    }

    public GstPojo(double sgst, double igst, double utgst,double shippingCharges,double discount) {
        this.sgst = sgst;
        this.igst = igst;
        this.utgst = utgst;
        this.shippingCharges = shippingCharges;
        this.discount = discount;
    }

    public double getUtgst() {
        return utgst;
    }

    public void setUtgst(double utgst) {
        this.utgst = utgst;
    }

    public double getIgst() {
        return igst;
    }

    public void setIgst(double igst) {
        this.igst = igst;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(double shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

}
