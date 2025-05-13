package com.product_comparator.productcomparator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptimizedShoppingBasketOutputDto {
    List<StoreTripDto> stores;
    BigDecimal totalCost;
    BigDecimal totalSavings;
}
