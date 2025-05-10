package com.product_comparator.productcomparator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
@Entity
public class Discount {
    @Id
    private String productId;
    private String productName;
    private String brand;
    private double packageQuantity;
    private String packageUnit;
    private String productCategory;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int percentage;
}
