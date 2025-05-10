package com.product_comparator.productcomparator.util;

import com.opencsv.CSVReader;
import com.product_comparator.productcomparator.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;


import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            List<Product> products = loadProducts(priceResource.getInputStream(), priceResource.getFilename());
            for(Product product : products){
                System.out.println(product);
            }
        }

    }

    private List<Product> loadProducts(InputStream filename, String store) throws IOException {
        List<Product> products = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(filename))){
            String[] nextLine, splitString;
            while((nextLine = csvReader.readNext()) != null){
                if(Arrays.toString(nextLine).contains("P")){
                    splitString = nextLine[0].split(";");
                    Product product = new Product(splitString[0], splitString[1], splitString[2], splitString[3],
                             Double.parseDouble(splitString[4]), splitString[5], Double.parseDouble(splitString[6]),
                            splitString[7], store.split("_")[0]);
                    products.add(product);

                }


            }
        }
        return products;
    }
}
