package com.product_comparator.productcomparator.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Data
@AllArgsConstructor
@Builder
public class StoreTripDto {
    private String storeName;
    List<ShoppingCartItemDto> items;
    BigDecimal subTotal;
    BigDecimal savings;

    public void updateTrip(ShoppingCartItemDto item, BigDecimal subTotal, BigDecimal savings) {
        this.items.add(item);
        this.subTotal = this.subTotal.add(subTotal);
        this.savings = this.savings.add(savings);
    }

    public StoreTripDto() {
        this.items = new ArrayList<>();
        this.subTotal = BigDecimal.ZERO;
        this.savings = BigDecimal.ZERO;
    }
}
