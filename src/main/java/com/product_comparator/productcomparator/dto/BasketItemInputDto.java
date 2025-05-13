package com.product_comparator.productcomparator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketItemInputDto {
    private String productName;
    private double quantity;
    private double quantitySI;
    private String unit;
    private String unitSI;

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
