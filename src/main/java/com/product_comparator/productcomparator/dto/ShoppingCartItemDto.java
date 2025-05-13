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
   private String productName;
   private double quantity;
   private double quantitySI;
   private  String unit;
   private  String unitSI;
   private  BigDecimal unitPrice;
   private  BigDecimal totalPrice;

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
