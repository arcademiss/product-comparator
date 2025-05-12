package com.product_comparator.productcomparator.controller;

import com.product_comparator.productcomparator.dto.DiscountDtoOutput;

import com.product_comparator.productcomparator.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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






    }

