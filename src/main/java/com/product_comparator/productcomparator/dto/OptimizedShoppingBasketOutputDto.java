package com.product_comparator.productcomparator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class OptimizedShoppingBasketOutputDto {
    List<StoreTripDto> stores;
    BigDecimal totalCost;
    BigDecimal totalSavings;


    public void updateBasket(Map<String, StoreTripDto> storeTripMap) {
        // update the shopping basket with store trip, total saving and total cost
        for (String key : storeTripMap.keySet()) {
            stores.add(storeTripMap.get(key));
            this.totalCost = this.totalCost.add(storeTripMap.get(key).getSubTotal()).setScale(2, RoundingMode.HALF_UP);
            this.totalSavings = this.totalSavings.add(storeTripMap.get(key).getSavings()).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public OptimizedShoppingBasketOutputDto() {
        // initialize to avoid null pointer exceptions
        this.stores = new ArrayList<>();
        this.totalCost = BigDecimal.ZERO;
        this.totalSavings = BigDecimal.ZERO;
    }
}
