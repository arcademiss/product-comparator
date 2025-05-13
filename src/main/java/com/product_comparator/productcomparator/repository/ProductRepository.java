package com.product_comparator.productcomparator.repository;

import com.product_comparator.productcomparator.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByProductIdAndStoreAndDate(String productId, String store, LocalDate date);
    List<Product> findByProductName(String name);
    List<Product> findByProductNameContainingIgnoreCase(String name);
}
