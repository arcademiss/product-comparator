package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InMemoryDataService {
    private final LocalDate currentDate = LocalDate.now();
    private List<Discount> discountList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();


    public void addProducts(List<Product> productList) {
        this.productList.addAll(productList);
    }
    public void addDiscounts(List<Discount> discountList) {
        this.discountList.addAll(discountList);
    }

//    public void printLists(){
//        System.out.println(discountList);
//        System.out.println(productList);
//    }

    public List<Discount> getActiveDiscounts(LocalDate date) {
        return discountList.stream()
                .filter(d -> (date.isAfter(d.getFromDate())) && date.isBefore(d.getToDate()))
                .toList();
        /*
           Should use current date here, but for simplicity of testing and avoiding changing the dates in the original
           datasets a date parameter is required in the POST request.
         */

    }

}
