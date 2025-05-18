package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.NewDiscountDto;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewDiscountService {
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    public List<NewDiscountDto> newDiscounts(LocalDate date) {
        List<NewDiscountDto> newDiscountDtos = new ArrayList<>();
        List<Discount> discounts = discountRepository.findByFromDateIn(List.of(date, date.minusDays(1)));
        for  (Discount discount : discounts) {
            Optional<Product> p = productRepository.findClosestByProductIdAndDateNative(discount.getProductId(), date);

            if(p.isPresent()) {
                BigDecimal price = BigDecimal.valueOf(p.get().getProductPrice());
                NewDiscountDto nD = NewDiscountDto.builder()
                        .productName(discount.getProductName())
                        .storeName(discount.getStore())
                        .discount(discount.getPercentage())
                        .fromDate(discount.getFromDate())
                        .toDate(discount.getToDate())
                        .quantity(discount.getNormalizedQuantity())
                        .unit(discount.getNormalizedUnit())
                        .oldPrice(price)
                        .newPrice(price
                                .multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(discount.getPercentage()/100.00)))
                                .setScale(2, RoundingMode.HALF_UP))
                        .build();
                newDiscountDtos.add(nD);
            }


        }
        return newDiscountDtos;
    }
}
