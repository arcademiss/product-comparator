package com.product_comparator.productcomparator.controller;

import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/discounts/{range}")
    public List<Discount> getDiscountsInRange(@PathVariable String range) {
        String[] dates = range.split("-");
        if (dates.length != 6) {
            throw new IllegalArgumentException("Expected format: yyyy-MM-dd-yyyy-MM-dd");
        }

        String startDateString = String.join("-", dates[0], dates[1], dates[2]);
        String endDateString = String.join("-", dates[3], dates[4], dates[5]);

        LocalDate startDate = LocalDate.parse(startDateString);
        LocalDate endDate = LocalDate.parse(endDateString);

        List<Discount> bestDiscounts = discountRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqual(
                startDate, endDate);

        bestDiscounts.sort(Comparator.comparing(Discount::getPercentage).reversed());

        return bestDiscounts;
    }



    }

