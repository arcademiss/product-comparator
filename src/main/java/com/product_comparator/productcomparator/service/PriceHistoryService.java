package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.PriceHistoryPointDto;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
            return Collections.emptyList();
            // todo raise exception 404
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
            return Collections.emptyList();
            // todo raise exception 404
        }

        List<PriceHistoryPointDto> priceHistoryPointDtos = new ArrayList<>();
        return priceHistoryPointDtos;
    }
}
