package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.NewDiscountDto;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewDiscountServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private DiscountRepository discountRepository;
    @InjectMocks
    private NewDiscountService newDiscountService;



    @Test
    void testNewDiscounts_WithMatchingProduct_ReturnsDtoList() {
        LocalDate testDate = LocalDate.of(2025, 5, 20);
        Discount discount = new Discount();
        discount.setProductId("P001");
        discount.setProductName("Milk");
        discount.setStore("Store A");
        discount.setPercentage(20);
        discount.setFromDate(testDate);
        discount.setToDate(testDate.plusDays(5));
        discount.setPackageQuantity(1.0);
        discount.setPackageUnit("l");

        Product product = new Product();
        product.setProductId("P001");
        product.setProductPrice(2.50);

        when(discountRepository.findByFromDateIn(List.of(testDate, testDate.minusDays(1))))
                .thenReturn(List.of(discount));

        when(productRepository.findClosestByProductIdAndDateNative("P001", testDate))
                .thenReturn(Optional.of(product));

        List<NewDiscountDto> result = newDiscountService.newDiscounts(testDate);

        assertEquals(1, result.size());
        NewDiscountDto dto = result.get(0);
        assertEquals("Milk", dto.getProductName());
        assertEquals("Store A", dto.getStoreName());
        assertEquals(20.0, dto.getDiscount());
        assertEquals(new BigDecimal("2.50"), dto.getOldPrice().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("2.00"), dto.getNewPrice().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void testNewDiscounts_WithNoMatchingProduct_ReturnsEmptyDtoList() {
        LocalDate testDate = LocalDate.of(2025, 5, 20);
        Discount discount = new Discount();
        discount.setProductId("P002");
        discount.setProductName("Bread");
        discount.setFromDate(testDate);
        discount.setPercentage(10);

        when(discountRepository.findByFromDateIn(List.of(testDate, testDate.minusDays(1))))
                .thenReturn(List.of(discount));

        when(productRepository.findClosestByProductIdAndDateNative("P002", testDate))
                .thenReturn(Optional.empty());

        List<NewDiscountDto> result = newDiscountService.newDiscounts(testDate);

        assertTrue(result.isEmpty());
    }

    @Test
    void testNewDiscounts_NoDiscounts_ReturnsEmptyList() {
        LocalDate testDate = LocalDate.of(2025, 5, 20);
        when(discountRepository.findByFromDateIn(List.of(testDate, testDate.minusDays(1))))
                .thenReturn(List.of());

        List<NewDiscountDto> result = newDiscountService.newDiscounts(testDate);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
