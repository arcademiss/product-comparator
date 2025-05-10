package com.product_comparator.productcomparator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Product {

    @Id
    private String productId;
    private String productName;
    private String productCategory;
    private String productBrand;
    private double packageQuantity;
    private String packageUnit;
    private double productPrice;
    private String currency;

}
