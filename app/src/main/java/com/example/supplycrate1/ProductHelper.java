package com.example.supplycrate1;

public class ProductHelper {
    String ProductName,ProductDetails,ProductQuantity,ProductUnit,ProductCategory,Mrp,Sellingprice,ProductImageUrl,Productkey;
    boolean stock;

    public ProductHelper(String productName, String productDetails, String productQuantity, String productUnit, String productCategory, String mrp, String sellingprice, String productImageUrl, String productkey, boolean stock) {
        ProductName = productName;
        ProductDetails = productDetails;
        ProductQuantity = productQuantity;
        ProductUnit = productUnit;
        ProductCategory = productCategory;
        Mrp = mrp;
        Sellingprice = sellingprice;
        ProductImageUrl = productImageUrl;
        Productkey = productkey;
        this.stock = stock;
    }

    public ProductHelper() {
    }

    public String getProductkey() {
        return Productkey;
    }

    public void setProductkey(String productkey) {
        Productkey = productkey;
    }

    public String getProductImageUrl() {
        return ProductImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        ProductImageUrl = productImageUrl;
    }

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getProductDetails() {
        return ProductDetails;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public String getProductUnit() {
        return ProductUnit;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public String  getMrp() {
        return Mrp;
    }

    public String getSellingprice() {
        return Sellingprice;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setProductDetails(String productDetails) {
        ProductDetails = productDetails;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }

    public void setProductUnit(String productUnit) {
        ProductUnit = productUnit;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public void setMrp(String mrp) {
        Mrp = mrp;
    }

    public void setSellingprice(String sellingprice) {
        Sellingprice = sellingprice;
    }
}
