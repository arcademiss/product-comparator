package com.product_comparator.productcomparator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductPriceController {

    @GetMapping("/hello")
    public Map<String, String> sayHello(){
        return Collections.singletonMap("hello", "world");
    }

    @GetMapping("/discounts")
    public void bestDiscounts(){

    }
}
