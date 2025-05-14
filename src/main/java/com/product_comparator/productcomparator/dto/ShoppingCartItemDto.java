package com.product_comparator.productcomparator.dto;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItemDto {
   private String productName;
   private double quantity;
   private  String unit;
   private  BigDecimal unitPrice;
   private  BigDecimal totalPrice;


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
