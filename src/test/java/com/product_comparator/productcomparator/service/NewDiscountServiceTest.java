package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.NewDiscountDto;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NewDiscountServiceTest {

    @Autowired
    private NewDiscountService newDiscountService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountRepository discountRepository;

    private final LocalDate testDate = LocalDate.of(2025, 5, 1);

    @BeforeEach
    void setUp() {
        // Clean up the database
        productRepository.deleteAll();
        discountRepository.deleteAll();

        // Add a product
        Product product = Product.builder()
                .productId("P001")
                .productName("Milk")
                .productPrice(5.0)
                .store("StoreA")
                .date(testDate.minusDays(1))
                .build();
        productRepository.save(product);

        // Add a discount
        Discount discount = Discount.builder()
                .productId("P001")
                .productName("Milk")
                .percentage(20)
                .store("StoreA")
                .fromDate(testDate)
                .toDate(testDate.plusDays(3))
                .packageQuantity(1.0)
                .packageUnit("L")
                .build();
        discountRepository.save(discount);
    }

    @Test
    void shouldReturnDiscountedProductDto() {
        List<NewDiscountDto> result = newDiscountService.newDiscounts(testDate);

        assertEquals(1, result.size());
        NewDiscountDto dto = result.get(0);

        assertEquals("Milk", dto.getProductName());
        assertEquals("StoreA", dto.getStoreName());
        assertEquals(20, dto.getDiscount());
        assertEquals(BigDecimal.valueOf(5.0).setScale(2, RoundingMode.HALF_UP), dto.getOldPrice().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(4.0).setScale(2, RoundingMode.HALF_UP), dto.getNewPrice().setScale(2, RoundingMode.HALF_UP));
        assertEquals("L", dto.getUnit());
        assertEquals(1.0, dto.getQuantity());
    }

    @Test
    void shouldReturnEmptyListIfNoMatchingDiscounts() {
        List<NewDiscountDto> result = newDiscountService.newDiscounts(LocalDate.of(2025, 6, 1));
        assertTrue(result.isEmpty());
    }
}
