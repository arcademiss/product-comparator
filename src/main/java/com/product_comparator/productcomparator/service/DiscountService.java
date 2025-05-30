package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.dto.DiscountDtoOutput;
import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.exception.discount.DiscountsNotFoundException;
import com.product_comparator.productcomparator.mapper.DiscountMapper;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@RequiredArgsConstructor
@Service
public class DiscountService {


    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;

    public List<DiscountDtoOutput> getDiscountsInRange(LocalDate startDate, LocalDate endDate) {
        // get active discounts from range and return a list with all discounts ordered by descending percentage
        List<DiscountDtoOutput> result = new ArrayList<>();
        List<Discount> discounts = discountRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqual(
                startDate, endDate);
        if (discounts.isEmpty()) {
            throw new DiscountsNotFoundException("Discounts not found!");
        }
        for (Discount discount : discounts) {
            result.add(discountMapper.discountToDiscountDtoOutput(discount));
        }


        result.sort(Comparator.comparing(DiscountDtoOutput::getPercentage).reversed());


        return result;

    }
}
