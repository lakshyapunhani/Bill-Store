package com.fabuleux.wuntu.billstore.Pojos;

public class ItemSelectionPojo
{
    private String productName;

    private String productRate;

    private String productDescription;

    private int numProducts;

    public ItemSelectionPojo() {
    }

    public int getNumProducts() {
        return numProducts;
    }

    public void setNumProducts(int numProducts) {
        this.numProducts = numProducts;
    }

    public ItemSelectionPojo(String productName, String productRate, String productDescription, int numProducts) {
        this.productName = productName;
        this.productRate = productRate;
        this.productDescription = productDescription;
        this.numProducts = numProducts;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductRate()
    {
        return productRate;
    }

    public void setProductRate(String productRate)
    {
        this.productRate = productRate;
    }

    public String getProductDescription()
    {
        return productDescription;
    }

    public void setProductDescription(String productDescription)
    {
        this.productDescription = productDescription;
    }
}
