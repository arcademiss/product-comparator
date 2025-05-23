package com.product_comparator.productcomparator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewDiscountDto {
    private String storeName;
    private String productName;
    private int discount;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private LocalDate fromDate;
    private LocalDate toDate;
    private double quantity;
    private String unit;

    // turn g to kg and ml to L
    public double getNormalizedQuantity() {
        return switch (unit.toLowerCase()) {
            case "g", "ml" -> quantity / 1000.0;
            default -> quantity;
        };
    }


    public String getNormalizedUnit() {
        return switch (unit.toLowerCase()) {
            case "g" -> "kg";
            case "ml" -> "l";
            default -> unit;
        };
    }
}
