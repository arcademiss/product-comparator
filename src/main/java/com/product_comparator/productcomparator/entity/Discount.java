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
    private int Id;
    private String productId;
    private String productName;
    private String brand;
    private double packageQuantity;
    private double packageQuantitySI;
    private String packageUnit;
    private String packageUnitSI;
    private String productCategory;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int percentage;
    private String store;

    public void normalizeUnits(){
        if(packageUnit.equals("g")){
            packageUnitSI = "kg";
            packageQuantitySI = packageQuantity/1000.00;
        }else {
            packageUnitSI = packageUnit;
            packageQuantitySI = packageQuantity;
        }
    }

}
