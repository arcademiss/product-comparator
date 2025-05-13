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
    String productName;
    double quantity;
    double quantitySI;
    String unit;
    String unitSI;

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
