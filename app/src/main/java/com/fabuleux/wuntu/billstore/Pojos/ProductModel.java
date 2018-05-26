package com.fabuleux.wuntu.billstore.Pojos;

public class ProductModel
{
    private String productId;

    private String productName;

    private String productRate;

    private String productDescription;

    public ProductModel()
    {}

    public ProductModel(String productId,String productName, String productRate, String productDescription)
    {
        this.productId = productId;
        this.productName = productName;
        this.productRate = productRate;
        this.productDescription = productDescription;
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
