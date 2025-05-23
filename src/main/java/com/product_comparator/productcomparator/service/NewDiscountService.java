package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.NewDiscountDto;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NewDiscountService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DiscountRepository discountRepository;

    public List<NewDiscountDto> newDiscounts(LocalDate date) {
        List<NewDiscountDto> newDiscountDtos = new ArrayList<>();

        // get discounts from the given date or a day before. In a real setting date should be LocalDate.now()
        // but it is a custom date to facilitate testing and demonstration
        List<Discount> discounts = discountRepository.findByFromDateIn(List.of(date, date.minusDays(1)));

        // loop through the discounts
        for (Discount discount : discounts) {

            // custom query to find the products by id and the date closest to the discount
            Optional<Product> p = productRepository.findClosestByProductIdAndDateNative(discount.getProductId(), date);

            // if there is a product that matches get the price and create the New discount dto to add to the list
            if (p.isPresent()) {
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
                        .newPrice(price // new price is price*(1-discount%)
                                .multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(discount.getPercentage() / 100.00)))
                                .setScale(2, RoundingMode.HALF_UP))
                        .build();

                newDiscountDtos.add(nD);
            }


        }
        return newDiscountDtos;
    }
}
