package com.product_comparator.productcomparator.controller;

import com.product_comparator.productcomparator.dto.BasketItemInputDto;
import com.product_comparator.productcomparator.dto.DiscountDtoOutput;

import com.product_comparator.productcomparator.dto.OptimizedShoppingBasketOutputDto;
import com.product_comparator.productcomparator.request.BasketRequest;
import com.product_comparator.productcomparator.service.DiscountService;
import com.product_comparator.productcomparator.service.OptimizedShoppingBasketService;
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
    private final OptimizedShoppingBasketService optimizedShoppingBasketService;



    @GetMapping("/discounts")
    public ResponseEntity<List<DiscountDtoOutput>> getAllDiscounts(
            @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(discountService.getDiscountsInRange(startDate, endDate));
    }


    @PostMapping("/optimize-basket")
    public ResponseEntity<OptimizedShoppingBasketOutputDto>  optimizeBasket(@RequestBody BasketRequest basketRequest) {
        List<BasketItemInputDto> items = basketRequest.getItems();
        LocalDate startDate = basketRequest.getStartDate();
        return ResponseEntity.ok(optimizedShoppingBasketService.getOptimizedBasket(items, startDate));
    }



    }

