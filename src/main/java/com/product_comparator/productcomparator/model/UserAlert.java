package com.product_comparator.productcomparator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserAlert {
    @Id
    private String productId;
    private double targetPrice;
    private String email;
}
