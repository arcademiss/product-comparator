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
    private double packageQuantitySI;
    private String packageUnit;
    private String packageUnitSI;
    private double productPrice;
    private String currency;
    private String store;
    private LocalDate date;



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
