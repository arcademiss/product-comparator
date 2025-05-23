package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.entity.UserAlert;
import com.product_comparator.productcomparator.exception.product.ProductNotFoundException;
import com.product_comparator.productcomparator.repository.ProductRepository;
import com.product_comparator.productcomparator.repository.UserAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class UserAlertService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserAlertRepository userAlertRepository;
    public String addUserAlert(
            String userEmail,
            String productName,
            String productBrand,
            String productStore,
            BigDecimal priceSetpoint
    ) {
        Product product = productRepository.findTop1ByProductNameAndProductBrandAndStore(productName, productBrand, productStore);
        if (product == null) {
            throw new ProductNotFoundException("Product not found");
        }
        UserAlert alert = UserAlert.builder()
                .userEmail(userEmail)
                .product(product)
                .dateAdded(LocalDate.now())
                .sent(false)
                .priceSetpoint(priceSetpoint)
                .build();
        userAlertRepository.save(alert);
        return "success";
    }
}
