package com.product_comparator.productcomparator.request;

import com.product_comparator.productcomparator.dto.BasketItemInputDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketRequest {
    private List<BasketItemInputDto> items;
    private LocalDate startDate;
}
