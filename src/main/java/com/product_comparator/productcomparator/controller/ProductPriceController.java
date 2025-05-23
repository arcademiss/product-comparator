package com.product_comparator.productcomparator.controller;

import com.product_comparator.productcomparator.dto.*;

import com.product_comparator.productcomparator.request.BasketRequest;
import com.product_comparator.productcomparator.service.*;
import lombok.RequiredArgsConstructor;

import org.mapstruct.control.MappingControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductPriceController {


    private final DiscountService discountService;
    private final OptimizedShoppingBasketService optimizedShoppingBasketService;
    private final NewDiscountService newDiscountService;
    private final PriceHistoryService priceHistoryService;
    private final BestBuysService bestBuysService;
    private final UserAlertService userAlertService;



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

    @GetMapping("/new-discounts")
    public ResponseEntity<List<NewDiscountDto>> getNewDiscounts(@RequestParam LocalDate date) {
        return ResponseEntity.ok(newDiscountService.newDiscounts(date));
    }

    @GetMapping("/price-history")
    public ResponseEntity<List<PriceHistoryPointDto>> getPriceHistory(
            @RequestParam String productName,
            @ModelAttribute PriceHistoryFilterRequest filter
    ) {
        String store = filter.getStore();
        String category = filter.getCategory();
        String brand = filter.getBrand();

        return ResponseEntity.ok(priceHistoryService.getHistory(productName, store, category, brand));
    }

    @GetMapping("/best-buys")
    public ResponseEntity<Map<String,List<BestBuyDto>>> getBestBuys(LocalDate date) {
        return ResponseEntity.ok(bestBuysService.bestBuys(date));
    }

    @PostMapping("/set-alert")
    public ResponseEntity<String> addAlert(
            @RequestBody UserAlertInputDto userAlertInput

    ) {
        String email = userAlertInput.getUserEmail();
        String name = userAlertInput.getProductName();
        String store = userAlertInput.getProductStore();
        String brand = userAlertInput.getProductBrand();
        BigDecimal price = userAlertInput.getPriceSetpoint();
        return ResponseEntity.ok(userAlertService.addUserAlert(email, name, store, brand, price));
    }

    }

