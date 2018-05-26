package com.fabuleux.wuntu.billstore.Pojos;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by roadcast on 3/5/18.
 */

public class ItemRealm extends RealmObject
{
    @PrimaryKey
    private String productId;

    private String productName;

    private String productRate;

    private String productDescription;

    private int numProducts;

    private String isSaved;

    public ItemRealm() {
    }

    public int getNumProducts() {
        return numProducts;
    }

    public void setNumProducts(int numProducts) {
        this.numProducts = numProducts;
    }

    public ItemRealm(String productId, String productName, String productRate, String productDescription, int numProducts, String isSaved) {
        this.productId = productId;
        this.productName = productName;
        this.productRate = productRate;
        this.productDescription = productDescription;
        this.numProducts = numProducts;
        this.isSaved = isSaved;
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

    public String getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(String isSaved) {
        this.isSaved = isSaved;
    }
}
