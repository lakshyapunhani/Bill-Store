package com.fabuleux.wuntu.billstore.Pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class ExtraDetailsPojo implements Parcelable
{
    int sgst;
    int igst;
    int utgst;
    int shipping_charges;
    int discount;
    boolean roundOff;

    public ExtraDetailsPojo() {
    }

    protected ExtraDetailsPojo(Parcel in) {
        sgst = in.readInt();
        igst = in.readInt();
        utgst = in.readInt();
        shipping_charges = in.readInt();
        discount = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sgst);
        dest.writeInt(igst);
        dest.writeInt(utgst);
        dest.writeInt(shipping_charges);
        dest.writeInt(discount);
        dest.writeByte((byte) (roundOff ? 1 : 0));
    }
}
