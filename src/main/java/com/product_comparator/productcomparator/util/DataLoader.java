package com.product_comparator.productcomparator.util;

import com.opencsv.CSVReader;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
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

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    public DataLoader(ProductRepository productRepository, DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }


    @PostConstruct
    public void init() throws IOException {
        Resource[] priceResources = resourcePatternResolver.getResources(csvPricesPath);
        for(Resource priceResource : priceResources){
            List<Product> products = getProductList(priceResource);

            for(Product product : products){
                if(!productRepository.existsByProductIdAndStoreAndDate(product.getProductId(), product.getStore(),
                        product.getDate())){
                    productRepository.save(product);
                }
            }


        }

        Resource[] discountResources = resourcePatternResolver.getResources(csvDiscountsPath);

        for(Resource discountResource : discountResources){
            List<Discount> discounts = loadDiscounts(discountResource.getInputStream(),
                    Objects.requireNonNull(discountResource.getFilename()).split("_")[0]);
            for(Discount discount : discounts){
                if (!discountRepository.existsByProductIdAndStoreAndFromDate(
                        discount.getProductId(), discount.getStore(), discount.getFromDate()
                )){
                    discountRepository.save(discount);
                }
            }

        }







    }

    private List<Product> getProductList(Resource priceResource) throws IOException {
        String dateString = Objects.requireNonNull(priceResource.getFilename()).split("_")[1].split("\\.")[0];
        LocalDate localDate = LocalDate.parse(dateString);
        return loadProducts(priceResource.getInputStream(), priceResource.getFilename(), localDate);
    }



    private List<Discount> loadDiscounts(InputStream filename, String store) throws IOException {
        List<Discount> discounts = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(filename))){
            String[] nextLine, splitString;
            while((nextLine = csvReader.readNext()) != null){
                if(Arrays.toString(nextLine).contains("P")){ //Checks if the line is a Discount and not a header
                    splitString = nextLine[0].split(";");
                    Discount discount = Discount.builder()
                            .productId(splitString[0])
                            .productName(splitString[1])
                            .brand(splitString[2])
                            .packageQuantity(Double.parseDouble(splitString[3]))
                            .packageUnit(splitString[4])
                            .productCategory(splitString[5])
                            .fromDate(LocalDate.parse(splitString[6]))
                            .toDate(LocalDate.parse(splitString[7]))
                            .percentage(Integer.parseInt(splitString[8]))
                            .store(store.split("_")[0])
                            .build();


                    discount.normalizeUnits();
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
                    Product product = Product.builder()

                            .productId(splitString[0])
                            .productName(splitString[1])
                            .productCategory(splitString[2])
                            .productBrand(splitString[3])
                            .packageQuantity(Double.parseDouble(splitString[4]))
                            .packageUnit(splitString[5])
                            .productPrice(Double.parseDouble(splitString[6]))
                            .currency(splitString[7])
                            .store(store.split("_")[0])
                            .build();
                    product.normalizeUnits();

                    product.setDate(date);

                    products.add(product);

                }


            }
        }
        return products;
    }
}
