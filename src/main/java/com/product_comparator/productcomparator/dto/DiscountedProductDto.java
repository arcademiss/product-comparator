package com.product_comparator.productcomparator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountedProductDto {
    private String productName;
    private String productCategory;
    private String productBrand;
    private double packageQuantity;
    private String packageUnit;
    private BigDecimal productPrice;
    private BigDecimal discountedPrice;
    private String store;


    public double getNormalizedQuantity() {
        return switch (packageUnit.toLowerCase()) {
            case "g", "ml" -> packageQuantity / 1000.0;
            default -> packageQuantity;
        };
    }


    public String getNormalizedUnit() {
        return switch (packageUnit.toLowerCase()) {
            case "g" -> "kg";
            case "ml" -> "l";
            default -> packageUnit;
        };
    }
}
