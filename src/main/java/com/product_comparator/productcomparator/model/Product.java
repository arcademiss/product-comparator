package com.product_comparator.productcomparator.model;





public class Product {


    private String productId;
    private String productName;
    private String productCategory;
    private String productBrand;
    private double packageQuantity;
    private String packageUnit;
    private double productPrice;
    private String currency;
    private String store;

    public Product() {
    }

    public Product(String productId, String productName, String productCategory, String productBrand,
                   double packageQuantity, String packageUnit, double productPrice, String currency, String store) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productBrand = productBrand;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
        this.productPrice = productPrice;
        this.currency = currency;
        this.store = store;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public double getPackageQuantity() {
        return packageQuantity;
    }

    public void setPackageQuantity(double packageQuantity) {
        this.packageQuantity = packageQuantity;
    }

    public String getPackageUnit() {
        return packageUnit;
    }

    public void setPackageUnit(String packageUnit) {
        this.packageUnit = packageUnit;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getStore() {
        return store;
    }
}
