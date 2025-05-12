package com.product_comparator.productcomparator.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@RequiredArgsConstructor
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItemDto {
    private String productName;
    private BigDecimal quantity;
    private String unit;
}
