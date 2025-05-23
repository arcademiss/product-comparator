package com.product_comparator.productcomparator.dto;

import com.product_comparator.productcomparator.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAlertInputDto {
    private String userEmail;
    private String productName;
    private String productBrand;
    private String productStore;
    private BigDecimal priceSetpoint;


}
