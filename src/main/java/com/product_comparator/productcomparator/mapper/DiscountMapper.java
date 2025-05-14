package com.product_comparator.productcomparator.mapper;

import com.product_comparator.productcomparator.dto.DiscountDtoOutput;
import com.product_comparator.productcomparator.entity.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper
public interface DiscountMapper {
    DiscountDtoOutput discountToDiscountDtoOutput(Discount discount);


}
