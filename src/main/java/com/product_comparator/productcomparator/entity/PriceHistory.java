package com.product_comparator.productcomparator.entity;





import java.time.LocalDate;


public class PriceHistory {

     private String productId;
     private LocalDate date;
    private double price;
    private String store;

    public PriceHistory() {
    }

    public PriceHistory(String productId, LocalDate date, double price,  String store) {
        this.productId = productId;
        this.date = date;
        this.price = price;
        this.store = store;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}

