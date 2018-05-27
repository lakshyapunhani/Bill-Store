package com.fabuleux.wuntu.billstore.Pojos;

public class ItemSelectionPojo
{

    private String productId;

    private String productName;

    private String productRate;

    private String productDescription;

    private int numProducts;



    public ItemSelectionPojo() {
    }

    public ItemSelectionPojo(ItemRealm itemRealm)
    {
        this.productId = itemRealm.getProductId();
        this.productName = itemRealm.getProductName();
        this.productRate = itemRealm.getProductRate();
        this.productDescription = itemRealm.getProductDescription();
        this.numProducts = itemRealm.getNumProducts();
    }

    public int getNumProducts() {
        return numProducts;
    }

    public void setNumProducts(int numProducts) {
        this.numProducts = numProducts;
    }

    public ItemSelectionPojo(String productId , String productName, String productRate, String productDescription, int numProducts) {
        this.productId = productId;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
