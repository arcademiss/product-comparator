package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.PriceHistoryPointDto;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.exception.product.ProductNotFoundException;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceHistoryServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private PriceHistoryService priceHistoryService;

    @Test
    void testGetHistory_WithGaps_AddsFullPricePoints() {
        Product product = new Product();
        product.setProductId("P001");
        product.setProductName("Gappy Product");
        product.setProductCategory("Category");
        product.setProductBrand("Brand");
        product.setStore("Store");
        product.setDate(LocalDate.of(2025, 5, 1));  // first seen on this date
        product.setCurrency("USD");
        product.setProductPrice(10.00);

        Discount discount1 = new Discount();
        discount1.setPercentage(20);
        discount1.setFromDate(LocalDate.of(2025, 5, 1));
        discount1.setToDate(LocalDate.of(2025, 5, 5));

        Discount discount2 = new Discount();
        discount2.setPercentage(10);
        discount2.setFromDate(LocalDate.of(2025, 5, 10));
        discount2.setToDate(LocalDate.of(2025, 5, 15));

        when(productRepository.findAll(any(Specification.class))).thenReturn(List.of(product));
        when(discountRepository.findByProductIdAndStoreOrderByFromDateAsc("P001", "Store"))
                .thenReturn(List.of(discount1, discount2));

        List<PriceHistoryPointDto> result = priceHistoryService.getHistory("Gappy Product", "Store", null, null);

        // Expect 4 points:
        // 1) Initial price
        // 2) Discounted point on 2025-05-01
        // 3) Full price on 2025-05-06 (gap after discount1 ends)
        // 4) Discounted point on 2025-05-10
        assertEquals(4, result.size());

        PriceHistoryPointDto point1 = result.get(0);
        assertEquals(LocalDate.of(2025, 5, 1), point1.getDate());
        assertEquals(BigDecimal.valueOf(8.00).setScale(2, RoundingMode.HALF_UP), point1.getPrice()); // 20% off

        PriceHistoryPointDto point2 = result.get(1);
        assertEquals(LocalDate.of(2025, 5, 6), point2.getDate());
        assertEquals(BigDecimal.valueOf(10.00).setScale(2, RoundingMode.HALF_UP), point2.getPrice()); // full price

        PriceHistoryPointDto point3 = result.get(2);
        assertEquals(LocalDate.of(2025, 5, 10), point3.getDate());
        assertEquals(BigDecimal.valueOf(9.00).setScale(2, RoundingMode.HALF_UP), point3.getPrice()); // 10% off
    }

    @Test
    void testGetHistory_WithoutDiscount_ReturnsOnlyFullPricePoint() {
        Product product = new Product();
        product.setProductId("P002");
        product.setProductName("No Discount Product");
        product.setProductCategory("Category");
        product.setProductBrand("Brand");
        product.setStore("Store");
        product.setDate(LocalDate.of(2025, 4, 1));
        product.setCurrency("EUR");
        product.setProductPrice(5.00);

        when(productRepository.findAll(any(Specification.class))).thenReturn(List.of(product));
        when(discountRepository.findByProductIdAndStoreOrderByFromDateAsc("P002", "Store"))
                .thenReturn(List.of());

        List<PriceHistoryPointDto> result = priceHistoryService.getHistory("No Discount", "Store", null, null);

        assertEquals(1, result.size());
        PriceHistoryPointDto point = result.get(0);
        assertEquals("No Discount Product", point.getProductName());
        assertEquals(BigDecimal.valueOf(5.00).setScale(2, RoundingMode.HALF_UP), point.getPrice());
        assertEquals(LocalDate.of(2025, 4, 1), point.getDate());
    }

    @Test
    void testGetHistory_ProductNotFound_ThrowsException() {
        when(productRepository.findAll(any(Specification.class))).thenReturn(List.of());

        assertThrows(ProductNotFoundException.class, () ->
                priceHistoryService.getHistory("Invalid", null, null, null));
    }
}
