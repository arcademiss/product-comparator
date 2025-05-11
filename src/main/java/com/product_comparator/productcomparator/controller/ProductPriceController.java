package com.product_comparator.productcomparator.controller;

import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductPriceController {

    private final DiscountRepository discountRepository;

    public ProductPriceController(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @GetMapping("/hello")
    public Map<String, String> sayHello(){
        return Collections.singletonMap("hello", "world");
    }

    @GetMapping("/discounts")
    public List<Discount> bestDiscounts() {
        List<Discount> currentDiscounts = discountRepository
                .findByFromDateLessThanEqualAndToDateGreaterThanEqual(LocalDate.parse("2025-05-03"), LocalDate.parse("2025-05-03"));


        currentDiscounts.sort(Comparator.comparing(Discount::getPercentage).reversed());

        return currentDiscounts;
    }
}
