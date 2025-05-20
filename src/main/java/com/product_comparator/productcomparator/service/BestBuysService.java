package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.BestBuyDto;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BestBuysService {
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<BestBuyDto> bestBuys(LocalDate date) {
        return null;
    }
}
