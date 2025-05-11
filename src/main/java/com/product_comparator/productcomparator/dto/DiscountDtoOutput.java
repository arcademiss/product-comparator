package com.product_comparator.productcomparator.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDtoOutput {
    private int id;
    private String productName;
    private String brand;
    private String store;
    private double packageQuantity;
    private String packageUnit;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int percentage;

}
