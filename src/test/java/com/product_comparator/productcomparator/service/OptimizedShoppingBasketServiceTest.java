package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.*;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.mapper.ProductMapper;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OptimizedShoppingBasketServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private DiscountRepository discountRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private OptimizedShoppingBasketService service;



    @Test
    void testGetOptimizedBasket_ReturnsCorrectOutput() {
        LocalDate date = LocalDate.of(2025, 5, 23);
        BasketItemInputDto basketItem = new BasketItemInputDto("Milk", 2.0, "l");
        Product product = new Product();
        product.setProductId("P001");
        product.setProductName("Milk 1L");
        product.setProductPrice(2.00);
        product.setStore("Store A");

        Discount discount = new Discount();
        discount.setPercentage(20);

        DiscountedProductDto discountedProductDto = DiscountedProductDto.builder()
                .productName("Milk 1L")
                .productPrice(BigDecimal.valueOf(2.00))
                .packageUnit("l")
                .store("Store A")
                .build();

        when(productRepository.findByProductNameContainingIgnoreCase("Milk"))
                .thenReturn(List.of(product));
        when(discountRepository.findByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                "P001", date, date, "Store A"
        )).thenReturn(discount);
        when(productMapper.productToDiscountedProductDto(product)).thenReturn(discountedProductDto);

        OptimizedShoppingBasketOutputDto output = service.getOptimizedBasket(List.of(basketItem), date);

        assertNotNull(output);
        assertEquals(1, output.getStores().size());

        StoreTripDto storeTrip = output.getStores().get(0);
        assertEquals("Store A", storeTrip.getStoreName());
        assertEquals(1, storeTrip.getItems().size());

        ShoppingCartItemDto cartItem = storeTrip.getItems().get(0);
        assertEquals("Milk 1L", cartItem.getProductName());
        assertEquals("l", cartItem.getUnit());
        assertEquals(BigDecimal.valueOf(1.60).setScale(2, RoundingMode.HALF_UP), cartItem.getUnitPrice().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(3.20).setScale(2, RoundingMode.HALF_UP), cartItem.getTotalPrice().setScale(2, RoundingMode.HALF_UP));

        assertEquals(BigDecimal.valueOf(3.20).setScale(2, RoundingMode.HALF_UP), storeTrip.getSubTotal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(BigDecimal.valueOf(0.80).setScale(2, RoundingMode.HALF_UP), storeTrip.getSavings().setScale(2, RoundingMode.HALF_UP)); // 0.40*2
    }

    @Test
    void testGetOptimizedBasket_NoMatchingProducts_ReturnsEmptyOutput() {
        LocalDate date = LocalDate.of(2025, 5, 23);
        BasketItemInputDto basketItem = new BasketItemInputDto("NonExistent", 1.0, "g");

        when(productRepository.findByProductNameContainingIgnoreCase("NonExistent"))
                .thenReturn(List.of());

        OptimizedShoppingBasketOutputDto output = service.getOptimizedBasket(List.of(basketItem), date);

        assertNotNull(output);
        assertTrue(output.getStores().isEmpty());
    }
}
