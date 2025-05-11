package com.product_comparator.productcomparator.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    private int Id;
    private String productId;
    private String productName;
    private String productCategory;
    private String productBrand;
    private double packageQuantity;
    private double packageQuantitySI;
    private String packageUnit;
    private String packageUnitSI;
    private double productPrice;
    private String currency;
    private String store;
    private LocalDate date;


}
