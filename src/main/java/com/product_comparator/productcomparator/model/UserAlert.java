package com.product_comparator.productcomparator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserAlert {
    @Id
    private String productId;
    private double targetPrice;
    private String email;

    public UserAlert(String productId, double targetPrice, String email) {
        this.productId = productId;
        this.targetPrice = targetPrice;
        this.email = email;
    }

    public UserAlert() {

    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
