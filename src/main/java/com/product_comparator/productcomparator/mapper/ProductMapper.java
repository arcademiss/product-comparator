package com.product_comparator.productcomparator.mapper;


import com.product_comparator.productcomparator.dto.DiscountedProductDto;
import com.product_comparator.productcomparator.entity.Product;
import org.mapstruct.Mapper;



@Mapper
public interface ProductMapper {
    DiscountedProductDto productToDiscountedProductDto(Product product);
    Product productToDiscountedProductDto(DiscountedProductDto discountedProductDto);


}
