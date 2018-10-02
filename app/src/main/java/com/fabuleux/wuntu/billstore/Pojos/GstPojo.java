package com.fabuleux.wuntu.billstore.Pojos;

public class GstPojo
{
    private double sgst;

    private double igst;

    private double utgst;

    public GstPojo() {
    }

    public GstPojo(double sgst, double igst, double utgst) {
        this.sgst = sgst;
        this.igst = igst;
        this.utgst = utgst;
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
}
