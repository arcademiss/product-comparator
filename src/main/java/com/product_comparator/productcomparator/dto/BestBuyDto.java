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
public class BestBuyDto {
    private String productId;
    private String productName;
    private String brand;
    private String unitNorm;
    private double quantityNorm;
    private BigDecimal price;
    private BigDecimal pricePerUnit;
}
