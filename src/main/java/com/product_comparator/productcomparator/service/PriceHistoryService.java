package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.PriceHistoryPointDto;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.exception.product.ProductNotFoundException;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceHistoryService {
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    public List<PriceHistoryPointDto> getHistory(
            String name,
            String store,
            String category,
            String brand
    ) {

        List<Product> products = productRepository.findByProductName(name);

        if (products.isEmpty()) {

            throw new ProductNotFoundException("Product not found");

        }

        if(store!=null) {
            products = products.stream()
                    .filter(product -> store.equalsIgnoreCase(product.getStore()))
                    .toList();
        }
        if(category!=null) {
            products = products.stream()
                    .filter(product -> category.equalsIgnoreCase(product.getProductCategory()))
                    .toList();
        }
        if(brand!=null) {
            products = products.stream()
                    .filter(product -> brand.equalsIgnoreCase(product.getProductBrand()))
                    .toList();
        }

        if(products.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        List<PriceHistoryPointDto> priceHistoryPointDtos = new ArrayList<>();

        for (Product product : products) {
            Discount activeDiscount = discountRepository
                    .findByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                            product.getProductId(),
                            product.getDate(),
                            product.getDate(),
                            product.getStore()
                    );
             PriceHistoryPointDto point = PriceHistoryPointDto.builder()
                    .productId(product.getProductId())
                    .productName(product.getProductName())
                    .productCategory(product.getProductCategory())
                    .productBrand(product.getProductBrand())
                    .productStore(product.getStore())
                    .date( activeDiscount != null ? activeDiscount.getFromDate() : product.getDate())
                    .currency(product.getCurrency())
                    .price(
                            BigDecimal.valueOf(product.getProductPrice())
                                    .multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(
                                            activeDiscount != null ? activeDiscount.getPercentage()/100.0 : 0)))
                                    .setScale(2, RoundingMode.HALF_UP)
                    )
                    .build();
             priceHistoryPointDtos.add(point);

        }

        return priceHistoryPointDtos.stream().sorted(Comparator.comparing(PriceHistoryPointDto::getDate)).toList();
    }
}
