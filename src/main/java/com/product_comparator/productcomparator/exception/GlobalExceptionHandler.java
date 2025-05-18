package com.product_comparator.productcomparator.exception;

import com.product_comparator.productcomparator.exception.discount.DiscountsNotFoundException;
import com.product_comparator.productcomparator.exception.product.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({DiscountsNotFoundException.class})
    public ResponseEntity<Object> discountsNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<Object> productNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
