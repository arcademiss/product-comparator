package com.product_comparator.productcomparator.util;

import com.opencsv.CSVReader;
import com.product_comparator.productcomparator.model.Discount;
import com.product_comparator.productcomparator.model.Product;
import com.product_comparator.productcomparator.service.InMemoryDataService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;



import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class DataLoader {
    @Value("${csv.prices.path}")
    private String csvPricesPath;
    @Value("${csv.discounts.path}")
    private String csvDiscountsPath;

    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private final InMemoryDataService inMemoryDataService = new InMemoryDataService();
    @PostConstruct
    public void init() throws IOException {
        Resource[] priceResources = resourcePatternResolver.getResources(csvPricesPath);
        for(Resource priceResource : priceResources){
            List<Product> products = getProductList(priceResource);
            inMemoryDataService.addProducts(products);
        }

        Resource[] discountResources = resourcePatternResolver.getResources(csvDiscountsPath);

        for(Resource discountResource : discountResources){
            List<Discount> discounts = getDiscountList(discountResource);
            inMemoryDataService.addDiscounts(discounts);
        }


//        inMemoryDataService.printLists();
        System.out.println(inMemoryDataService.getActiveDiscounts(LocalDate.now()));


    }

    private List<Product> getProductList(Resource priceResource) throws IOException {
        String dateString = Objects.requireNonNull(priceResource.getFilename()).split("_")[1].split("\\.")[0];
        LocalDate localDate = LocalDate.parse(dateString);
        return loadProducts(priceResource.getInputStream(), priceResource.getFilename(), localDate);
    }

    private List<Discount> getDiscountList(Resource discountResource) throws IOException {


        return loadDiscounts(discountResource.getInputStream(), discountResource.getFilename()
                );
    }

    private List<Discount> loadDiscounts(InputStream filename, String store) throws IOException {
        List<Discount> discounts = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(filename))){
            String[] nextLine, splitString;
            while((nextLine = csvReader.readNext()) != null){
                if(Arrays.toString(nextLine).contains("P")){ //Checks if the line is a Discount and not a header
                    splitString = nextLine[0].split(";");
                    Discount discount = new Discount(splitString[0], splitString[1], splitString[2],
                            Double.parseDouble(splitString[3]),splitString[4], splitString[5],
                            LocalDate.parse(splitString[6]), LocalDate.parse(splitString[7]),
                            Integer.parseInt(splitString[8]), store.split("_")[0]);


                    discounts.add(discount);

                }


            }
        }
        return discounts;
    }

    private List<Product> loadProducts(InputStream filename, String store, LocalDate date) throws IOException {
        List<Product> products = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(filename))){
            String[] nextLine, splitString;
            while((nextLine = csvReader.readNext()) != null){
                if(Arrays.toString(nextLine).contains("P")){ //Checks if the line is a Product and not a header
                    splitString = nextLine[0].split(";");
                    Product product = new Product(splitString[0], splitString[1], splitString[2], splitString[3],
                             Double.parseDouble(splitString[4]), splitString[5], Double.parseDouble(splitString[6]),
                            splitString[7], store.split("_")[0]);

                    product.setDate(date);

                    products.add(product);

                }


            }
        }
        return products;
    }
}
