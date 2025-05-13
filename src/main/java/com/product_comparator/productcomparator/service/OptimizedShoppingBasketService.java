package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.BasketItemInputDto;
import com.product_comparator.productcomparator.dto.DiscountDtoOutput;
import com.product_comparator.productcomparator.dto.DiscountedProductDto;
import com.product_comparator.productcomparator.dto.OptimizedShoppingBasketOutputDto;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.mapper.ProductMapper;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OptimizedShoppingBasketService {
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final ProductMapper productMapper;

    public OptimizedShoppingBasketOutputDto getOptimizedBasket(List<BasketItemInputDto> items, LocalDate date) {
        //  Get data from database for products and discounts
        //  Create discounted items
        List<DiscountedProductDto> discountedProducts = new ArrayList<>();
        for (BasketItemInputDto item : items) {
            List<Product> products = productRepository.findByProductNameContainingIgnoreCase(item.getProductName());

            if (products.isEmpty()) {
                System.out.println("No product with name " + item.getProductName() + " found"); // Change later with exc
                continue;// move to the next item in the cart

            }

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
                int discountPercentage = disc != null ? disc.getPercentage() : 0;

                discountedProduct.setProductPrice(
                        currentPrice.subtract(currentPrice.multiply(BigDecimal.valueOf(discountPercentage)))
                );

                discountedProducts.add(discountedProduct);
            }

        }

        // TODO Optimize the shopping basket
        // TODO Create object for return


        return optimizeBasket(discountedProducts);
    }

    private OptimizedShoppingBasketOutputDto optimizeBasket(List<DiscountedProductDto> discountedProducts) {
        return OptimizedShoppingBasketOutputDto.builder().build();
    }

}
