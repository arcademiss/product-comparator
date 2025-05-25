package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.BestBuyDto;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BestBuysServiceTest {

    @Mock
    private DiscountRepository discountRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private BestBuysService bestBuysService;

    @Test
    void bestBuys_withDiscount() {
        LocalDate date = LocalDate.of(2025, 5, 7);
        Product product = Product.builder()
                .productId("P001")
                .productName("Milk")
                .productBrand("BrandA")
                .packageQuantity(1.0)
                .productPrice(2.00)
                .store("StoreA")
                .packageUnit("l")
                .date(date)
                .build();

        Discount discount = Discount.builder()
                .productId("P001")
                .percentage(10)
                .store("StoreA")
                .fromDate(date.minusDays(1))
                .toDate(date.plusDays(1))
                .build();

        when(productRepository.findLowestPricedProducts(date)).thenReturn(List.of(product));
        when(discountRepository.findTop1ByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                "P001",date,date,"StoreA"
        )).thenReturn(discount);

        Map<String, List<BestBuyDto>> result = bestBuysService.bestBuys(date);

        assertEquals(1, result.size());
        List<BestBuyDto> bestBuyDtos = result.get("Milk");
        assertNotNull(bestBuyDtos);
        assertEquals(1, bestBuyDtos.size());
        assertEquals(new BigDecimal("1.80"), bestBuyDtos.getFirst().getPrice());
        assertEquals(new BigDecimal("1.80"), bestBuyDtos.getFirst().getPricePerUnit());
    }

    @Test
    void bestBuys_withoutDiscount() {
        LocalDate date = LocalDate.of(2025, 5, 7);
        Product product = Product.builder()
                .productId("P001")
                .productName("Milk")
                .productBrand("BrandA")
                .packageQuantity(1.0)
                .productPrice(2.00)
                .store("StoreA")
                .packageUnit("l")
                .date(date)
                .build();



        when(productRepository.findLowestPricedProducts(date)).thenReturn(List.of(product));
        when(discountRepository.findTop1ByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                "P001",date,date,"StoreA"
        )).thenReturn(null);

        Map<String, List<BestBuyDto>> result = bestBuysService.bestBuys(date);

        assertEquals(1, result.size());
        List<BestBuyDto> bestBuyDtos = result.get("Milk");
        assertNotNull(bestBuyDtos);
        assertEquals(1, bestBuyDtos.size());
        assertEquals(new BigDecimal("2.00"), bestBuyDtos.getFirst().getPrice());
        assertEquals(new BigDecimal("2.00"), bestBuyDtos.getFirst().getPricePerUnit());
    }
}