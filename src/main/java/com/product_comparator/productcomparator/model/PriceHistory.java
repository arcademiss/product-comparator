package com.product_comparator.productcomparator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class PriceHistory {
    @Id
    private String productId;
    private LocalDate date;
    private double price;

    public PriceHistory() {
    }

    public PriceHistory(String productId, LocalDate date, double price) {
        this.productId = productId;
        this.date = date;
        this.price = price;
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
}
