package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.entity.UserAlert;
import com.product_comparator.productcomparator.exception.product.ProductNotFoundException;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import com.product_comparator.productcomparator.repository.UserAlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAlertServiceTest {

    private ProductRepository productRepository;
    private UserAlertRepository userAlertRepository;
    private DiscountRepository discountRepository;
    private UserAlertService userAlertService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        userAlertRepository = mock(UserAlertRepository.class);
        discountRepository = mock(DiscountRepository.class);
        userAlertService = new UserAlertService(productRepository, userAlertRepository, discountRepository);

    }

    @Test
    void testAddUserAlert_Success() {
        Product product = Product.builder()
                .productId("P001")
                .productName("Milk")
                .productBrand("BrandA")
                .store("StoreX")
                .productPrice(3.50)
                .build();

        when(productRepository.findTop1ByProductNameAndProductBrandAndStore("Milk", "BrandA", "StoreX"))
                .thenReturn(product);

        String result = userAlertService.addUserAlert("user@example.com", "Milk", "BrandA", "StoreX", BigDecimal.valueOf(2.99));

        assertEquals("success", result);
        verify(userAlertRepository, times(1)).save(any(UserAlert.class));
    }

    @Test
    void testAddUserAlert_ProductNotFound() {
        when(productRepository.findTop1ByProductNameAndProductBrandAndStore("Milk", "BrandA", "StoreX"))
                .thenReturn(null);

        assertThrows(ProductNotFoundException.class, () ->
                userAlertService.addUserAlert("user@example.com", "Milk", "BrandA", "StoreX", BigDecimal.valueOf(2.99))
        );
    }

    @Test
    void testCheckAndSendEmails_AlertMetWithDiscount() {
        Product product = Product.builder()
                .productId("P001")
                .productName("Milk")
                .store("StoreX")
                .productPrice(5.00)
                .build();

        UserAlert alert = UserAlert.builder()
                .userEmail("user@example.com")
                .product(product)
                .priceSetpoint(BigDecimal.valueOf(4.00))
                .sent(false)
                .build();

        Discount discount = Discount.builder()
                .percentage(25)
                .build();

        when(userAlertRepository.findBySent(false)).thenReturn(List.of(alert));
        when(discountRepository.findTop1ByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                eq("P001"), any(LocalDate.class), any(LocalDate.class), eq("StoreX")))
                .thenReturn(discount);

        userAlertService.checkAndSendEmails();

        assertTrue(alert.getSent());
        assertEquals(LocalDate.now(), alert.getDateSent());
        verify(userAlertRepository).save(alert);
    }

    @Test
    void testCheckAndSendEmails_AlertNotMetWithoutDiscount() {
        Product product = Product.builder()
                .productId("P001")
                .productName("Milk")
                .store("StoreX")
                .productPrice(5.00)
                .build();

        UserAlert alert = UserAlert.builder()
                .userEmail("user@example.com")
                .product(product)
                .priceSetpoint(BigDecimal.valueOf(3.00))
                .sent(false)
                .build();

        when(userAlertRepository.findBySent(false)).thenReturn(List.of(alert));
        when(discountRepository.findTop1ByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                anyString(), any(LocalDate.class), any(LocalDate.class), anyString())).thenReturn(null);

        userAlertService.checkAndSendEmails();

        assertFalse(alert.getSent());
        verify(userAlertRepository, never()).save(alert);
    }
}
