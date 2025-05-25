package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.DiscountDtoOutput;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.exception.discount.DiscountsNotFoundException;
import com.product_comparator.productcomparator.mapper.DiscountMapper;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class DiscountServiceTest {

    @InjectMocks
    private DiscountService discountService;
    @Mock
    private DiscountRepository discountRepository;
    @Mock
    private DiscountMapper discountMapper;



    @Test
    void getDiscountsInRange_returnSortedDiscounts() {
        // Given
        LocalDate start = LocalDate.of(2025, 5, 6);
        LocalDate end = LocalDate.of(2025, 5, 10);

        Discount d1 = Discount.builder().percentage(30).build();
        Discount d2 = Discount.builder().percentage(10).build();
        Discount d3 = Discount.builder().percentage(20).build();

        List<Discount> mockDiscounts = Arrays.asList(d1, d2, d3);
        when(discountRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqual(start, end))
                .thenReturn(mockDiscounts);

        when(discountMapper.discountToDiscountDtoOutput(d1)).thenReturn(DiscountDtoOutput.builder().id(1).percentage(10).build());
        when(discountMapper.discountToDiscountDtoOutput(d2)).thenReturn(DiscountDtoOutput.builder().id(1).percentage(20).build());
        when(discountMapper.discountToDiscountDtoOutput(d3)).thenReturn(DiscountDtoOutput.builder().id(1).percentage(30).build());

        List<DiscountDtoOutput> result = discountService.getDiscountsInRange(start, end);

        assertEquals(3, result.size());
        assertEquals(30, result.get(0).getPercentage());
        assertEquals(20, result.get(1).getPercentage());
        assertEquals(10, result.get(2).getPercentage());


    }

    @Test
    void getDiscountsInRange_throwsExceptionWhenNoDiscountsFound() {
        LocalDate start = LocalDate.of(2025, 5, 6);
        LocalDate end = LocalDate.of(2025, 5, 10);
        when(discountRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqual(start, end))
                .thenReturn(Collections.emptyList());

        assertThrows(DiscountsNotFoundException.class, () -> {
            discountService.getDiscountsInRange(start, end);
        });
    }
}