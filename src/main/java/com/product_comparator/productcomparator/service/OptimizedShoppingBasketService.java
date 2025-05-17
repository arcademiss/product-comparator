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
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        for (BasketItemInputDto item : items) {

            List<Product> products = productRepository.findByProductNameContainingIgnoreCase(item.getProductName());

            if (products.isEmpty()) {

                continue;// move to the next item in the cart

            }

            List<DiscountedProductDto> discountedProducts= new ArrayList<>();
            for (Product product : products) {

                DiscountedProductDto discountedProduct = productMapper.productToDiscountedProductDto(product);
                Discount disc = discountRepository
                    .findByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                            product.getProductId(),
                            date,
                            date,
                            product.getStore()
                    );

                BigDecimal currentPrice = discountedProduct.getProductPrice();
                // if disc != null first option else second

                double discountPercentage = disc != null ? disc.getPercentage()/100.0 : 0;




                discountedProduct.setDiscountedPrice(
                        currentPrice.subtract(currentPrice.multiply(BigDecimal.valueOf(discountPercentage)))
                );


                discountedProducts.add(discountedProduct);


            }
            discountedProducts.sort(Comparator.comparing(DiscountedProductDto::getDiscountedPrice));
            DiscountedProductDto cheapestProduct = discountedProducts.getFirst();

            ShoppingCartItemDto shoppingCartItemDto = ShoppingCartItemDto.builder()
                    .productName(cheapestProduct.getProductName())
                    .quantity(item.getQuantity())
                    .unit(cheapestProduct.getNormalizedUnit())
                    .unitPrice(cheapestProduct.getDiscountedPrice()
                            .setScale(2, RoundingMode.HALF_UP))
                    .totalPrice(BigDecimal.valueOf(item.getQuantity()).multiply(cheapestProduct.getDiscountedPrice()).setScale(2, RoundingMode.HALF_UP))
                    .build();

            storeTripMap
                    .computeIfAbsent(cheapestProduct.getStore(), k -> new StoreTripDto())
                    .setStoreName(cheapestProduct.getStore());

            storeTripMap.get(cheapestProduct.getStore())
                    .updateTrip(shoppingCartItemDto, shoppingCartItemDto.getTotalPrice(),
                            cheapestProduct.getProductPrice().subtract(cheapestProduct.getDiscountedPrice()));



        }

        OptimizedShoppingBasketOutputDto output = new OptimizedShoppingBasketOutputDto();

        output.updateBasket(storeTripMap);



        return output;
    }

    private OptimizedShoppingBasketOutputDto optimizeBasket(List<DiscountedProductDto> discountedProducts) {
        return OptimizedShoppingBasketOutputDto.builder().build();
    }

}
