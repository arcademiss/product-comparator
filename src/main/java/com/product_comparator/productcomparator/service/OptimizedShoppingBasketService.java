package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.BasketItemInputDto;
import com.product_comparator.productcomparator.dto.OptimizedShoppingBasketOutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OptimizedShoppingBasketService {
    public OptimizedShoppingBasketOutputDto getOptimizedBasket(List<BasketItemInputDto> items, LocalDate date) {
        // TODO Get data from database for products and discounts

        // TODO Create discounted items
        // TODO Optimize the shopping basket
        // TODO Create object for return
        return OptimizedShoppingBasketOutputDto.builder().build();
    }

}
