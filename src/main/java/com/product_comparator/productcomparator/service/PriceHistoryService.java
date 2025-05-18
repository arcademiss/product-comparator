package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.PriceHistoryPointDto;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
        return Collections.emptyList();
    }
}
