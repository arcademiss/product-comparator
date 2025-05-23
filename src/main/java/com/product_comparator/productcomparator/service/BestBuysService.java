package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.BestBuyDto;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
public class BestBuysService {
    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductRepository productRepository;

    public BestBuysService(DiscountRepository discountRepository, ProductRepository productRepository) {
        this.discountRepository = discountRepository;
        this.productRepository = productRepository;
    }



    public Map<String,List<BestBuyDto>> bestBuys(LocalDate date) {
        // map with product name as keys and value is a list of bestbuydtos
        Map<String,List<BestBuyDto>> map = new HashMap<>();

        List<Product> products = productRepository.findLowestPricedProducts(date);

        // loop through the products
        for(Product product:products){

            // get the discount for the product
            Discount d = discountRepository.findTop1ByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                    product.getProductId(),
                    product.getDate(),
                    product.getDate(),
                    product.getStore()
            );

            // Compute discounted price:
            // If discount exists apply discount percentage if not keep original price
            BigDecimal priceDiscounted = BigDecimal.valueOf(product.getProductPrice())
                    .multiply(BigDecimal.ONE.subtract(d != null ? BigDecimal.valueOf(d.getPercentage()/100.0) : BigDecimal.ZERO))
                    .setScale(2, RoundingMode.HALF_UP);

            // Create BestBuyDto with computed price
            BestBuyDto bestBuyDto = BestBuyDto.builder()
                    .productId(product.getProductId())
                    .productName(product.getProductName())
                    .brand(product.getProductBrand())
                    .quantityNorm(product.getNormalizedQuantity())
                    .unitNorm(product.getNormalizedUnit())
                    .price(priceDiscounted)
                    .pricePerUnit(
                            // avoid dividing by zero
                            product.getNormalizedQuantity() > 0 ? priceDiscounted.divide(
                                    BigDecimal.valueOf(product.getNormalizedQuantity()),2, RoundingMode.HALF_UP)
                                    : BigDecimal.ZERO
                    )
                    .store(product.getStore())
                    .build();

            // group by product name, create a new list if product is not in the keys
            map.computeIfAbsent(product.getProductName(), k -> new ArrayList<>()).add(bestBuyDto);

        }

        // for each list in the map, sort it by ascending price per unit
        for(List<BestBuyDto> list:map.values()){
            list.sort(Comparator.comparing(BestBuyDto::getPricePerUnit));
        }
        return map;
    }
}
