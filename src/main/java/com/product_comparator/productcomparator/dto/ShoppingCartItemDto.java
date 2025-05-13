package com.product_comparator.productcomparator.dto;

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
    String productName;
    double quantity;
    double quantitySI;
    String unit;
    String unitSI;
    BigDecimal unitPrice;
    BigDecimal totalPrice;

    public void normalizeUnits(){
        if(Objects.equals(unit, "g")) {
            unitSI="kg";
            quantitySI=quantity/1000;

        }else {
            unitSI=unit;
            quantitySI=quantity;
        }
    }

}
