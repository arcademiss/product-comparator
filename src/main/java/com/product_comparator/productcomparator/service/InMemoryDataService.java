package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.model.Discount;
import com.product_comparator.productcomparator.model.Product;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class InMemoryDataService {
    private List<List<Discount>> discountList = new ArrayList<>();
    private List<List<Product>> productList = new ArrayList<>();


    public void addProducts(List<Product> productList) {
        this.productList.add(productList);
    }
    public void addDiscounts(List<Discount> discountList) {
        this.discountList.add(discountList);
    }

    public void printLists(){
        System.out.println(discountList);
        System.out.println(productList);
    }
}
