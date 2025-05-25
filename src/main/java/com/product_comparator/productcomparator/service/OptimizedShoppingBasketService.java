package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.*;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.mapper.ProductMapper;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.math.RoundingMode;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OptimizedShoppingBasketService {
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final ProductMapper productMapper;

    public OptimizedShoppingBasketOutputDto getOptimizedBasket(List<BasketItemInputDto> items, LocalDate date) {
        //  Get data from database for products and discounts
        //  Create discounted items

        Map<String, StoreTripDto> storeTripMap = new HashMap<>();
        // loop through the products in the user's basket
        for (BasketItemInputDto item : items) {

            // find products with name "LIKE" the name of the products in the basket
            List<Product> products = productRepository.findByProductNameContainingIgnoreCase(item.getProductName());

            if (products.isEmpty()) {

                continue;// move to the next item in the cart

            }

            List<DiscountedProductDto> discountedProducts= new ArrayList<>();

            // loop through the products
            for (Product product : products) {

                // find the discount for the product in the given time period(again to simulate the behaviour of
                // LocalDate.now()) and store
                DiscountedProductDto discountedProduct = productMapper.productToDiscountedProductDto(product);
                Discount disc = discountRepository
                    .findByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                            product.getProductId(),
                            date,
                            date,
                            product.getStore()
                    );

                BigDecimal currentPrice = discountedProduct.getProductPrice();

                // compute the discount in % if it exists, if it doesn't it's 0
                double discountPercentage = disc != null ? disc.getPercentage()/100.0 : 0;


                // set the discounted price and add it to the list

                discountedProduct.setDiscountedPrice(
                        currentPrice.subtract(currentPrice.multiply(BigDecimal.valueOf(discountPercentage)))
                );


                discountedProducts.add(discountedProduct);


            }
            // sort the list by the discounted price to get the cheapest product
            discountedProducts.sort(Comparator.comparing(DiscountedProductDto::getDiscountedPrice));
            if(discountedProducts.isEmpty()){continue;}
            DiscountedProductDto cheapestProduct = discountedProducts.getFirst();

            // create the shopping cart item with the cheapest product
            ShoppingCartItemDto shoppingCartItemDto = ShoppingCartItemDto.builder()
                    .productName(cheapestProduct.getProductName())
                    .quantity(item.getQuantity())
                    .unit(cheapestProduct.getNormalizedUnit())
                    .unitPrice(cheapestProduct.getDiscountedPrice()
                            .setScale(2, RoundingMode.HALF_UP))
                    .totalPrice(BigDecimal.valueOf(item.getQuantity()).multiply(cheapestProduct.getDiscountedPrice()).setScale(2, RoundingMode.HALF_UP))
                    .build();
            // group the shopping cart items by store names and create the empty list and key if the key doesn't exist
            storeTripMap
                    .computeIfAbsent(cheapestProduct.getStore(), k -> new StoreTripDto())
                    .setStoreName(cheapestProduct.getStore());

            // update the store sub-total and total savings and list of products
            storeTripMap.get(cheapestProduct.getStore())
                    .updateTrip(shoppingCartItemDto, shoppingCartItemDto.getTotalPrice(),
                            cheapestProduct.getProductPrice().subtract(cheapestProduct.getDiscountedPrice()
                                    ).multiply(BigDecimal.valueOf(item.getQuantity())));



        }

        OptimizedShoppingBasketOutputDto output = new OptimizedShoppingBasketOutputDto();
        // update the optimized basket with the store trip
        output.updateBasket(storeTripMap);



        return output;
    }


}
