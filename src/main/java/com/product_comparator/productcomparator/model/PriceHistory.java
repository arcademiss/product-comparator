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
}
