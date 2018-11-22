package com.fabuleux.wuntu.billstore.Pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class ExtraDetailsPojo implements Parcelable
{
    int sgst;
    int igst;
    int utgst;
    double shipping_charges;
    double discount;
    boolean roundOff;

    public ExtraDetailsPojo() {
    }

    protected ExtraDetailsPojo(Parcel in) {
        sgst = in.readInt();
        igst = in.readInt();
        utgst = in.readInt();
        shipping_charges = in.readDouble();
        discount = in.readDouble();
        roundOff = in.readByte() != 0;
    }

    public static final Creator<ExtraDetailsPojo> CREATOR = new Creator<ExtraDetailsPojo>() {
        @Override
        public ExtraDetailsPojo createFromParcel(Parcel in) {
            return new ExtraDetailsPojo(in);
        }

        @Override
        public ExtraDetailsPojo[] newArray(int size) {
            return new ExtraDetailsPojo[size];
        }
    };

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

    public double getShipping_charges() {
        return shipping_charges;
    }

    public void setShipping_charges(double shipping_charges) {
        this.shipping_charges = shipping_charges;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public boolean isRoundOff() {
        return roundOff;
    }

    public void setRoundOff(boolean roundOff) {
        this.roundOff = roundOff;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sgst);
        dest.writeInt(igst);
        dest.writeInt(utgst);
        dest.writeDouble(shipping_charges);
        dest.writeDouble(discount);
        dest.writeByte((byte) (roundOff ? 1 : 0));
    }
}
