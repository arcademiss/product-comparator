package com.product_comparator.productcomparator.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"productId", "store", "fromDate"})
)
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String productId;
    private String productName;
    private String brand;
    private double packageQuantity;
    private String packageUnit;
    private String productCategory;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int percentage;
    private String store;

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
