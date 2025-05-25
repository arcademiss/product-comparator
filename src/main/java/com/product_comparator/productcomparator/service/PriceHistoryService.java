package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.PriceHistoryPointDto;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.exception.product.ProductNotFoundException;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import com.product_comparator.productcomparator.repository.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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

        // create a specification based on the available filters. Only the name is required, the rest are ignored if null
        Specification<Product> spec = Specification
                .where(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasStore(store))
                .and(ProductSpecification.hasCategory(category))
                .and(ProductSpecification.hasBrand(brand));

        // find all products that meet the specification
        List<Product> products = productRepository.findAll(spec);

        if (products.isEmpty()) {

            throw new ProductNotFoundException("Product not found");

        }





        List<PriceHistoryPointDto> priceHistoryPointDtos = new ArrayList<>();
        // loop through the products
        for (Product product : products) {

            // get the active discount for the product
            List<Discount> discounts = discountRepository
                    .findByProductIdAndStoreOrderByFromDateAsc(product.getProductId(), product.getStore());

            LocalDate productStartDate = product.getDate(); // earliest known date for the product
            LocalDate previousEndDate = productStartDate;

            for (Discount discount : discounts) {
                // Fill in gap before this discount, if any
                if (discount.getFromDate().isAfter(previousEndDate)) {
                    // Add a point for the start of the gap
                    priceHistoryPointDtos.add(PriceHistoryPointDto.builder()
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            .productCategory(product.getProductCategory())
                            .productBrand(product.getProductBrand())
                            .productStore(product.getStore())
                            .date(previousEndDate)
                            .currency(product.getCurrency())
                            .price(
                                    BigDecimal.valueOf(product.getProductPrice())
                                            .setScale(2, RoundingMode.HALF_UP)
                            )
                            .build());
                }

                // Add the discounted point
                priceHistoryPointDtos.add(PriceHistoryPointDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .productCategory(product.getProductCategory())
                        .productBrand(product.getProductBrand())
                        .productStore(product.getStore())
                        .date(discount.getFromDate())
                        .currency(product.getCurrency())
                        .price(
                                BigDecimal.valueOf(product.getProductPrice())
                                        .multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(discount.getPercentage() / 100.0)))
                                        .setScale(2, RoundingMode.HALF_UP)
                        )
                        .build());

                // Move the previousEndDate to the day after this discount ends
                previousEndDate = discount.getToDate().plusDays(1);
            }

// Optionally, add one final full-price point after the last discount
            if (previousEndDate.isBefore(LocalDate.now())) {
                priceHistoryPointDtos.add(PriceHistoryPointDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .productCategory(product.getProductCategory())
                        .productBrand(product.getProductBrand())
                        .productStore(product.getStore())
                        .date(previousEndDate)
                        .currency(product.getCurrency())
                        .price(
                                BigDecimal.valueOf(product.getProductPrice())
                                        .setScale(2, RoundingMode.HALF_UP)
                        )
                        .build());
            }







        }
        // return a sorted list by date, to make historical data progress naturally
        return priceHistoryPointDtos.stream().sorted(Comparator.comparing(PriceHistoryPointDto::getDate)).toList();
    }
}
