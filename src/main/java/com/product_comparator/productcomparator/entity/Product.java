package com.product_comparator.productcomparator.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"Id", "store", "date"})
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
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<UserAlert> alerts;


    // transform g to kg and ml to L
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
