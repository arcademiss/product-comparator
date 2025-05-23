package com.product_comparator.productcomparator.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserAlertService {
    public String addUserAlert(
            String userEmail,
            String productName,
            String productBrand,
            String productStore,
            BigDecimal priceSetpoint
    ) {
        return "success";
    }
}
