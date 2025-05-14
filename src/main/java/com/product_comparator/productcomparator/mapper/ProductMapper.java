package com.product_comparator.productcomparator.mapper;


import com.product_comparator.productcomparator.dto.DiscountedProductDto;
import com.product_comparator.productcomparator.entity.Product;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface ProductMapper {
    DiscountedProductDto productToDiscountedProductDto(Product product);



}
