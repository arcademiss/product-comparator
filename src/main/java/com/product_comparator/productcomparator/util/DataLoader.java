package com.product_comparator.productcomparator.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class DataLoader {
    @Value("${csv.prices.path}")
    private String csvPricesPath;
    @Value("${csv.discounts.path}")
    private String csvDiscountsPath;

    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    @PostConstruct
    public void init() throws IOException {
        Resource[] priceResources = resourcePatternResolver.getResources(csvPricesPath);
        for(Resource priceResource : priceResources){
            System.out.println(priceResource.getFilename());
        }

    }
}
