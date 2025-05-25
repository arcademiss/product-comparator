package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.PriceHistoryPointDto;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.exception.product.ProductNotFoundException;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PriceHistoryServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private DiscountRepository discountRepository;
    @InjectMocks
    private PriceHistoryService priceHistoryService;



    @Test
    void testGetHistory_WithDiscount_AppliesDiscountCorrectly() {
        Product product = new Product();
        product.setProductId("P001");
        product.setProductName("Test Product");
        product.setProductCategory("Category");
        product.setProductBrand("Brand");
        product.setStore("Store");
        product.setDate(LocalDate.of(2025, 5, 1));
        product.setCurrency("USD");
        product.setProductPrice(10.00);

        Discount discount = new Discount();
        discount.setPercentage(20);
        discount.setFromDate(LocalDate.of(2025, 5, 1));

        when(productRepository.findAll(any(Specification.class))).thenReturn(List.of(product));
        when(discountRepository.findByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                "P001", product.getDate(), product.getDate(), product.getStore()
        )).thenReturn(discount);

        List<PriceHistoryPointDto> result = priceHistoryService.getHistory("Test", "Store", "Category", "Brand");

        assertEquals(1, result.size());
        PriceHistoryPointDto point = result.get(0);
        assertEquals("Test Product", point.getProductName());
        assertEquals(LocalDate.of(2025, 5, 1), point.getDate());
        assertEquals(BigDecimal.valueOf(8.00).setScale(2, RoundingMode.HALF_UP), point.getPrice()); // 10 * 0.8
    }

    @Test
    void testGetHistory_WithoutDiscount_ReturnsOriginalPrice() {
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
        when(discountRepository.findByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                "P002", product.getDate(), product.getDate(), product.getStore()
        )).thenReturn(null);

        List<PriceHistoryPointDto> result = priceHistoryService.getHistory("No Discount", "Store", null, null);

        assertEquals(1, result.size());
        PriceHistoryPointDto point = result.get(0);
        assertEquals("No Discount Product", point.getProductName());
        assertEquals(BigDecimal.valueOf(5.00).setScale(2, RoundingMode.HALF_UP), point.getPrice());
    }

    @Test
    void testGetHistory_ProductNotFound_ThrowsException() {
        when(productRepository.findAll(any(Specification.class))).thenReturn(List.of());

        assertThrows(ProductNotFoundException.class, () ->
                priceHistoryService.getHistory("Invalid", null, null, null));
    }
}
