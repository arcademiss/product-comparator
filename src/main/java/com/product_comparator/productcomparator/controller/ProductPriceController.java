package com.product_comparator.productcomparator.controller;

import com.product_comparator.productcomparator.dto.DiscountDtoOutput;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductPriceController {


    private final DiscountService discountService;


    @GetMapping("/discounts")
    public ResponseEntity<List<DiscountDtoOutput>> getAllDiscounts(
            @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(discountService.getDiscountsInRange(startDate, endDate));
    }


    @PostMapping("/basket/optimize")
    public ResponseEntity<List<DiscountDtoOutput>> optimizeBasket(){
        return null;
    }


    }

