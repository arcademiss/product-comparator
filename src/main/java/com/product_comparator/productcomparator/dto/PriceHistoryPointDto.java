package com.product_comparator.productcomparator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceHistoryPointDto {

    private String productId;
    private String productName;
    private String productCategory;
    private String productBrand;
    private String productStore;
    private LocalDate date;
    private BigDecimal price;
    private String currency;

}
