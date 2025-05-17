package com.product_comparator.productcomparator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
        for (String key : storeTripMap.keySet()) {
            stores.add(storeTripMap.get(key));
            this.totalCost = this.totalCost.add(storeTripMap.get(key).getSubTotal());
            this.totalSavings = this.totalSavings.add(storeTripMap.get(key).getSavings());
        }
    }

    public OptimizedShoppingBasketOutputDto() {
        this.stores = new ArrayList<>();
        this.totalCost = BigDecimal.ZERO;
        this.totalSavings = BigDecimal.ZERO;
    }
}
