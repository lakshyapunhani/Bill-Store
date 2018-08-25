package com.fabuleux.wuntu.billstore.Pojos;

public class ContactPojo
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
}
