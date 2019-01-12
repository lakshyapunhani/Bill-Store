package com.fabuleux.wuntu.billstore.Pojos;

import java.util.Map;

public class InvoicePojo
{
    private ContactPojo receiverPojo;

    private ContactPojo senderPojo;

    private String invoiceNumber;

    private double billAmount;

    private Map<String,ItemPojo> billItems;

    private String invoiceDate;

    private String dueDate;

    private GstPojo gstPojo;

    private String billDescription;

    private String billStatus;

    private String billType;

    private String billTime;

    private Map<String,String> billImages;

    private String paidOn;

    private String cancelledOn;

    public InvoicePojo() {
    }

    public InvoicePojo(ContactPojo receiverPojo,ContactPojo senderPojo, String invoiceNumber,
                       double billAmount, Map<String, ItemPojo> billItems, String invoiceDate,
                       String dueDate, GstPojo gstPojo, String billDescription, String billStatus, String billType
                        , String billTime, Map<String, String> billImages,String paidOn,String cancelledOn)
    {
        this.receiverPojo = receiverPojo;
        this.senderPojo = senderPojo;
        this.invoiceNumber = invoiceNumber;
        this.billAmount = billAmount;
        this.billItems = billItems;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.gstPojo = gstPojo;
        this.billDescription = billDescription;
        this.billStatus = billStatus;
        this.billType = billType;
        this.billTime = billTime;
        this.billImages = billImages;
        this.paidOn = paidOn;
        this.cancelledOn = cancelledOn;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public Map<String, ItemPojo> getBillItems() {
        return billItems;
    }

    public void setBillItems(Map<String, ItemPojo> billItems) {
        this.billItems = billItems;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public GstPojo getGstPojo() {
        return gstPojo;
    }

    public void setGstPojo(GstPojo gstPojo) {
        this.gstPojo = gstPojo;
    }

    public String getBillDescription() {
        return billDescription;
    }

    public void setBillDescription(String billDescription) {
        this.billDescription = billDescription;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getBillTime() {
        return billTime;
    }

    public void setBillTime(String billTime) {
        this.billTime = billTime;
    }

    public Map<String, String> getBillImages() {
        return billImages;
    }

    public void setBillImages(Map<String, String> billImages) {
        this.billImages = billImages;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public ContactPojo getReceiverPojo() {
        return receiverPojo;
    }

    public void setReceiverPojo(ContactPojo receiverPojo) {
        this.receiverPojo = receiverPojo;
    }

    public ContactPojo getSenderPojo() {
        return senderPojo;
    }

    public void setSenderPojo(ContactPojo senderPojo) {
        this.senderPojo = senderPojo;
    }

    public String getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(String paidOn) {
        this.paidOn = paidOn;
    }

    public String getCancelledOn() {
        return cancelledOn;
    }

    public void setCancelledOn(String cancelledOn) {
        this.cancelledOn = cancelledOn;
    }
}
