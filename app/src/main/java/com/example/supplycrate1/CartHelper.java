package com.example.supplycrate1;

public class CartHelper {
    String Productname,Productunit,Productprice,ProductCategory,ProductImageUrl,ProductKey,ProductQuantity,ProductCartKey;

    public CartHelper() {
    }

    public CartHelper(String productname, String productunit, String productprice, String productCategory, String productImageUrl, String productKey, String productQuantity, String productCartKey) {
        Productname = productname;
        Productunit = productunit;
        Productprice = productprice;
        ProductCategory = productCategory;
        ProductImageUrl = productImageUrl;
        ProductKey = productKey;
        ProductQuantity = productQuantity;
        ProductCartKey = productCartKey;
    }

    public String getProductCartKey() {
        return ProductCartKey;
    }

    public void setProductCartKey(String productCartKey) {
        ProductCartKey = productCartKey;
    }

    public String getProductImageUrl() {
        return ProductImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        ProductImageUrl = productImageUrl;
    }

    public String getProductKey() {
        return ProductKey;
    }

    public void setProductKey(String productKey) {
        ProductKey = productKey;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }

    public String getProductname() {
        return Productname;
    }

    public void setProductname(String productname) {
        Productname = productname;
    }

    public String getProductunit() {
        return Productunit;
    }

    public void setProductunit(String productunit) {
        Productunit = productunit;
    }

    public String getProductprice() {
        return Productprice;
    }

    public void setProductprice(String productprice) {
        Productprice = productprice;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }
}
