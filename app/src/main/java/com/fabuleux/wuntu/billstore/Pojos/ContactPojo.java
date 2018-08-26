package com.fabuleux.wuntu.billstore.Pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactPojo implements Parcelable
{
    private String contactName;

    private String contactAddress;

    private String contactGstNumber;

    private String contactPhoneNumber;

    public ContactPojo() {
    }

    public ContactPojo(String contactName, String contactAddress, String contactGstNumber, String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
        this.contactName = contactName;
        this.contactAddress = contactAddress;
        this.contactGstNumber = contactGstNumber;
    }

    protected ContactPojo(Parcel in) {
        contactName = in.readString();
        contactAddress = in.readString();
        contactGstNumber = in.readString();
        contactPhoneNumber = in.readString();
    }

    public static final Creator<ContactPojo> CREATOR = new Creator<ContactPojo>() {
        @Override
        public ContactPojo createFromParcel(Parcel in) {
            return new ContactPojo(in);
        }

        @Override
        public ContactPojo[] newArray(int size) {
            return new ContactPojo[size];
        }
    };

    public String getContactName() {
        return contactName;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public String getContactGstNumber() {
        return contactGstNumber;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contactName);
        dest.writeString(contactAddress);
        dest.writeString(contactGstNumber);
        dest.writeString(contactPhoneNumber);
    }
}
