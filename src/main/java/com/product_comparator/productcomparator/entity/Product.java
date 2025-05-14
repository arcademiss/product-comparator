package com.product_comparator.productcomparator.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"productId", "store", "date"})
)
public class Product {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String productId;
    private String productName;
    private String productCategory;
    private String productBrand;
    private double packageQuantity;
    private String packageUnit;
    private double productPrice;
    private String currency;
    private String store;
    private LocalDate date;



    @Transient
    public double getNormalizedQuantity() {
        return switch (packageUnit.toLowerCase()) {
            case "g", "ml" -> packageQuantity / 1000.0;
            default -> packageQuantity;
        };
    }


    @Transient
    public String getNormalizedUnit() {
        return switch (packageUnit.toLowerCase()) {
            case "g" -> "kg";
            case "ml" -> "l";
            default -> packageUnit;
        };
    }




}
